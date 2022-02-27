package net.moddingplayground.macaws.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.moddingplayground.macaws.api.sound.MacawsSoundEvents;
import net.moddingplayground.macaws.impl.entity.HeadMountAccess;

public abstract class TameableHeadEntity extends TameableEntity {
    private long lastUseTime = 0;

    protected TameableHeadEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public long getLastUseTime() {
        return this.lastUseTime;
    }

    public boolean hasBeenRecentlyUsed() {
        return this.world.getTime() - this.getLastUseTime() < 20;
    }

    public boolean hasOwnerForceUsed(PlayerEntity owner) {
        return this.hasBeenRecentlyUsed() && this.canOwnerForceUse(owner);
    }

    public boolean canOwnerForceUse(PlayerEntity owner) {
        return this.distanceTo(owner) < 5.0d;
    }

    public boolean isCollidingWith(Entity other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }

    public boolean mountOnto(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("id", this.getSavedEntityId());
        this.writeNbt(nbt);
        if (((HeadMountAccess) player).addHeadEntity(nbt)) {
            player.world.playSound(null, player.getX(), player.getY(), player.getZ(), MacawsSoundEvents.ENTITY_MACAW_MOUNT_ON, this.getSoundCategory(), 0.3f, 0.9f + (this.random.nextFloat() * 0.2f));
            this.discard();
            return true;
        }

        return false;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            if (player == this.getOwner() && this.canOwnerForceUse(player)) {
                this.lastUseTime = this.world.getTime();
                return ActionResult.success(this.world.isClient);
            }
        }
        return super.interactMob(player, hand);
    }

    public static class SitOnOwnerHeadGoal extends Goal {
        private final TameableHeadEntity mob;
        private ServerPlayerEntity owner;
        private boolean mounted;

        public SitOnOwnerHeadGoal(TameableHeadEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            if (this.mob.isSitting()) return false;
            return this.mob.getOwner() instanceof HeadMountAccess access && access.canHeadMount(this.mob);
        }

        @Override
        public boolean canStop() {
            return !this.mounted;
        }

        @Override
        public void start() {
            this.owner = (ServerPlayerEntity)this.mob.getOwner();
            this.mounted = false;
        }

        @Override
        public void tick() {
            if (this.mounted || this.mob.isInSittingPose() || this.mob.isLeashed()) return;
            if (this.mob.hasOwnerForceUsed(this.owner) || this.mob.isCollidingWith(this.owner)) this.mounted = this.mob.mountOnto(this.owner);
        }
    }
}
