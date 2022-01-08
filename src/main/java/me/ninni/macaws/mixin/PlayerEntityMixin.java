package me.ninni.macaws.mixin;

import me.ninni.macaws.entity.MacawEntity;
import me.ninni.macaws.entity.MacawsEntities;
import me.ninni.macaws.entity.access.HeadMountAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.ninni.macaws.sound.MacawsSoundEvents.*;
import static me.ninni.macaws.util.MacawsNbtConstants.*;
import static net.minecraft.nbt.NbtElement.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HeadMountAccess {
    @Shadow public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    private long headEntityAddedTime;
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
            NbtCompound nbt = this.getHeadEntity();
            if (nbt != null && EntityType.get(nbt.getString("id")).filter(type -> type == MacawsEntities.MACAW).isPresent()) {
                if (this.random.nextInt(1000) < this.mountedMacawAmbientSoundChance++) {
                    MacawEntity.Personality personality = MacawEntity.Personality.readFromNbt(nbt);
                    boolean eyepatch = nbt.getBoolean(NBT_HAS_EYEPATCH);
                    boolean isInWaterBoat = this.getRootVehicle() instanceof BoatEntityAccessor boat && boat.getLocation() == BoatEntity.Location.IN_WATER;

                    this.playSound(
                        eyepatch && isInWaterBoat ? ENTITY_MACAW_AMBIENT_EYEPATCH : ENTITY_MACAW_AMBIENT_TAMED,
                        SoundCategory.NEUTRAL, 1.0f, personality.pitch()
                    );

                    this.resetMountedMacawAmbientSoundChance();
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
    public void tryDropHeadEntity() {
        if (this.headEntityAddedTime + 20L < this.world.getTime()) {
            this.dropHeadEntity(this.getHeadEntity());
            this.setHeadEntity(new NbtCompound());
            this.resetMountedMacawAmbientSoundChance();
        }
    }

    @Override
    public void dropHeadEntity(NbtCompound nbt) {
        if (!this.world.isClient && !nbt.isEmpty()) {
            EntityType.getEntityFromNbt(nbt, this.world).ifPresent(entity -> {
                if (entity instanceof TameableEntity tameable) tameable.setOwnerUuid(this.uuid);
                entity.setPosition(this.getX(), this.getY() + (double)0.7f, this.getZ());
                ((ServerWorld)this.world).tryLoadEntity(entity);
                if (entity instanceof LivingEntity livingEntity) livingEntity.setHealth(nbt.getFloat("Health"));
            });
        }
    }

    @Override
    public boolean canHeadMount() {
        return !this.isTouchingWater() && !this.inPowderSnow;
    }
}
