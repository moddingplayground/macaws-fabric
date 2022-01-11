package me.ninni.macaws.mixin;

import me.ninni.macaws.Macaws;
import me.ninni.macaws.entity.MacawEntity;
import me.ninni.macaws.entity.MacawsEntities;
import me.ninni.macaws.entity.access.HeadMountAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

import static me.ninni.macaws.entity.MacawEntity.*;
import static me.ninni.macaws.sound.MacawsSoundEvents.*;
import static me.ninni.macaws.util.MacawsNbtConstants.*;
import static net.minecraft.nbt.NbtElement.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HeadMountAccess {
    private long headEntityAddedTime;
    private long lastMacawSpeechTime;
    private int mountedMacawAmbientSoundChance;

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private void resetMountedMacawAmbientSoundChance() {
        this.mountedMacawAmbientSoundChance = -120;
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onTickMovement(CallbackInfo ci) {
        if (!this.world.isClient) {
            if (!this.canHeadMount()) {
                this.tryDropHeadEntity();
            } else {
                NbtCompound nbt = this.getHeadEntity();
                if (nbt != null && EntityType.get(nbt.getString("id")).filter(type -> type == MacawsEntities.MACAW).isPresent()) {
                    if (!nbt.getBoolean(NBT_SILENT) && this.random.nextInt(1000) < this.mountedMacawAmbientSoundChance++) {
                        boolean eyepatch = nbt.getBoolean(NBT_HAS_EYEPATCH);
                        boolean isInWaterBoat = this.getRootVehicle() instanceof BoatEntityAccessor boat && boat.getLocation() == BoatEntity.Location.IN_WATER;
                        SoundEvent sound = eyepatch && isInWaterBoat ? ENTITY_MACAW_AMBIENT_EYEPATCH : ENTITY_MACAW_AMBIENT_TAMED;
                        Personality personality = Personality.readFromNbt(nbt);
                        this.world.playSoundFromEntity(null, this, sound, SoundCategory.NEUTRAL, 1.0f, personality.pitch());

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
        if (!this.canHeadMount()) return false;

        if (this.getHeadEntity().isEmpty()) {
            this.setHeadEntity(nbt);
            this.headEntityAddedTime = this.world.getTime();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryDropHeadEntity(Vec3d pos) {
        if (this.canHeadDismount()) {
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
                }
            });
        }
    }

    @Override
    public boolean canHeadMount() {
        return !this.isTouchingWater() && !this.isInLava() && !this.inPowderSnow && !this.isSpectator();
    }

    @Override
    public boolean canHeadDismount() {
        return !this.getHeadEntity().isEmpty() && this.headEntityAddedTime + 20L < this.world.getTime();
    }

    private static final Function<Item, Identifier> ITEM_TO_SOUND = Util.memoize(item -> {
        Identifier id = Registry.ITEM.getId(item);
        return new Identifier(Macaws.MOD_ID, "entity.macaw.speech.item.%s".formatted(id.toUnderscoreSeparatedString()));
    });

    @Override
    public void onNovelItemPickUp(ItemStack stack) {
        long time = this.world.getTime();
        if (this.world instanceof ServerWorld serverWorld && this.lastMacawSpeechTime + 13L < time) {
            // get personality from stored macaw
            NbtCompound nbt = this.getHeadEntity();
            Personality personality = Personality.readFromNbt(nbt);

            // create packet
            Identifier id = ITEM_TO_SOUND.apply(stack.getItem());
            PlaySoundIdS2CPacket packet = new PlaySoundIdS2CPacket(id, SoundCategory.NEUTRAL, this.getPos(), 1.0f, personality.pitch());

            // send packet
            MinecraftServer server = serverWorld.getServer();
            PlayerManager playerManager = server.getPlayerManager();
            RegistryKey<World> worldKey = this.world.getRegistryKey();
            playerManager.sendToAround(null, this.getX(), this.getY(), this.getZ(), 16.0f, worldKey, packet);

            // delay
            this.lastMacawSpeechTime = time;
        }
    }
}
