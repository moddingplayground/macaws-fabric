package net.moddingplayground.macaws.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.moddingplayground.macaws.api.client.entity.MacawSpeechCallback;
import net.moddingplayground.macaws.api.entity.MacawEntity;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;
import net.moddingplayground.macaws.api.entity.TameableHeadEntity;
import net.moddingplayground.macaws.impl.entity.HeadMountAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.nbt.NbtElement.*;
import static net.moddingplayground.macaws.api.entity.MacawEntity.*;
import static net.moddingplayground.macaws.api.sound.MacawsSoundEvents.*;
import static net.moddingplayground.macaws.api.util.MacawsNbtConstants.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HeadMountAccess {
    private int mountedMacawAmbientSoundChance;

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private void resetMountedMacawAmbientSoundChance() {
        this.mountedMacawAmbientSoundChance = -120;
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onTickMovement(CallbackInfo ci) {
        PlayerEntity that = PlayerEntity.class.cast(this);
        if (!this.world.isClient) {
            if (!this.canHeadMount(null)) {
                this.tryDropHeadEntity();
            } else {
                NbtCompound nbt = this.getHeadEntity();
                if (nbt != null && EntityType.get(nbt.getString("id")).filter(type -> type == MacawsEntityType.MACAW).isPresent()) {
                    if (!nbt.getBoolean(NBT_SILENT) && this.random.nextInt(1000) < this.mountedMacawAmbientSoundChance++) {
                        boolean hasEyepatch = nbt.getBoolean(NBT_HAS_EYEPATCH);
                        boolean isRowingBoat = this.getRootVehicle() instanceof BoatEntityAccessor boat && boat.getLocation() == BoatEntity.Location.IN_WATER;
                        boolean pirate = hasEyepatch && isRowingBoat;

                        boolean playToThis = pirate || this.random.nextInt(3) == 0;
                        SoundEvent sound = pirate ? ENTITY_MACAW_AMBIENT_EYEPATCH : ENTITY_MACAW_AMBIENT_TAMED;
                        Personality personality = Personality.readFromNbt(nbt.getCompound(NBT_PERSONALITY));

                        this.world.playSoundFromEntity(playToThis ? null : that, this, sound, SoundCategory.NEUTRAL, 1.0f, personality.pitch());
                        this.resetMountedMacawAmbientSoundChance();
                    }
                }
            }
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void onInitDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(HEAD_ENTITY, new NbtCompound());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound headEntityNbt = this.getHeadEntity();
        if (!headEntityNbt.isEmpty()) nbt.put(NBT_HEAD_ENTITY, headEntityNbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(NBT_HEAD_ENTITY, COMPOUND_TYPE)) this.setHeadEntity(nbt.getCompound(NBT_HEAD_ENTITY));
    }

    @Override
    public NbtCompound getHeadEntity() {
        return this.dataTracker.get(HEAD_ENTITY);
    }

    @Override
    public void setHeadEntity(NbtCompound nbt) {
        this.dataTracker.set(HEAD_ENTITY, nbt);
    }

    @Override
    public boolean addHeadEntity(NbtCompound nbt) {
        if (!this.canHeadMount(null)) return false;

        if (this.getHeadEntity().isEmpty()) {
            this.setHeadEntity(nbt);
            return true;
        }

        return false;
    }

    @Override
    public boolean tryDropHeadEntity(Vec3d pos) {
        if (!this.getHeadEntity().isEmpty() && this.canHeadDismount()) {
            this.dropHeadEntity(this.getHeadEntity(), pos);
            this.setHeadEntity(new NbtCompound());
            this.resetMountedMacawAmbientSoundChance();
            return true;
        }
        return false;
    }

    @Override
    public boolean tryDropHeadEntity() {
        return this.tryDropHeadEntity(new Vec3d(this.getX(), this.getY() + 0.7D, this.getZ()));
    }

    @Override
    public void dropHeadEntity(NbtCompound nbt, Vec3d pos) {
        if (!this.world.isClient && !nbt.isEmpty()) {
            EntityType.getEntityFromNbt(nbt, this.world).ifPresent(entity -> {
                if (entity instanceof MacawEntity macaw) {
                    macaw.setOwnerUuid(this.uuid);
                    macaw.setPosition(pos);
                    ((ServerWorld)this.world).tryLoadEntity(macaw);
                    macaw.setHealth(nbt.getFloat("Health"));
                    macaw.setVelocity(Vec3d.ZERO);
                }
            });
        }
    }

    /**
     * @param entity nullable for if an entity object is not available or to bypass checks
     */
    @Override
    public boolean canHeadMount(@Nullable TameableHeadEntity entity) {
        PlayerEntity that = (PlayerEntity) (Object) this;
        if (this.isSubmergedInWater() || this.isSubmergedIn(FluidTags.LAVA)) return false;
        if (this.inPowderSnow) return false;
        if (this.isSpectator()) return false;
        return entity == null || (entity.hasOwnerForceUsed(that) || entity.getVelocity().length() > 0.08d);
    }

    @Override
    public boolean canHeadDismount() {
        return true;
    }

    @Override
    public void onNovelItemPickUp(ItemStack stack) {
        PlayerEntity that = (PlayerEntity) (Object) this;
        if (that instanceof ServerPlayerEntity splayer) {
            UUID uuid = this.getUuid();
            NbtCompound nbt = this.getHeadEntity();
            NbtCompound personality = nbt.getCompound(NBT_PERSONALITY);

            List<ServerPlayerEntity> players = new ArrayList<>(PlayerLookup.tracking(that));
            players.add(splayer);
            for (ServerPlayerEntity player : players) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(uuid);
                buf.writeNbt(personality);
                buf.writeItemStack(stack);

                ServerPlayNetworking.send(player, MacawSpeechCallback.PACKET_ID, buf);
            }

            this.resetMountedMacawAmbientSoundChance();
        }
    }
}
