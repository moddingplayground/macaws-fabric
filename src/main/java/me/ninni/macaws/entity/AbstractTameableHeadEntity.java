package me.ninni.macaws.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

// TODO
public abstract class AbstractTameableHeadEntity extends TameableEntity {
    private static final int READY_TO_SIT_COOLDOWN = 100;
    private int ticks;

    protected AbstractTameableHeadEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean mountOnto(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("id", this.getSavedEntityId());
        this.writeNbt(nbt);
        if (player.addShoulderEntity(nbt)) {
            this.discard();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        this.ticks++;
        super.tick();
    }

    public boolean isReadyToSitOnPlayer() {
        return this.ticks > READY_TO_SIT_COOLDOWN;
    }
}
