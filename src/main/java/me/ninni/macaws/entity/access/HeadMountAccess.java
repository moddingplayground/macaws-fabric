package me.ninni.macaws.entity.access;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public interface HeadMountAccess {
    TrackedData<NbtCompound> HEAD_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

    NbtCompound getHeadEntity();
    void setHeadEntity(NbtCompound nbt);
    boolean addHeadEntity(NbtCompound nbt);

    boolean tryDropHeadEntity(Vec3d pos);
    boolean tryDropHeadEntity();
    void dropHeadEntity(NbtCompound nbt, Vec3d pos);

    boolean canHeadMount();
    boolean canHeadDismount();
}
