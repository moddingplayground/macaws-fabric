package net.moddingplayground.macaws.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.moddingplayground.macaws.entity.access.HeadMountAccess;
import net.moddingplayground.macaws.sound.MacawsSoundEvents;

public abstract class TameableHeadEntity extends TameableEntity {
    private static final int READY_TO_SIT_COOLDOWN = 100;
    private int ticks;

    protected TameableHeadEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
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
    public void tick() {
        this.ticks++;
        super.tick();
    }

    public boolean isReadyToSitOnPlayer() {
        return this.ticks > READY_TO_SIT_COOLDOWN;
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
            return this.mob.getOwner() instanceof HeadMountAccess access && access.canHeadMount() && !this.mob.isSitting() && this.mob.isReadyToSitOnPlayer();
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
            if (this.mob.getBoundingBox().intersects(this.owner.getBoundingBox())) this.mounted = this.mob.mountOnto(this.owner);
        }
    }
}
