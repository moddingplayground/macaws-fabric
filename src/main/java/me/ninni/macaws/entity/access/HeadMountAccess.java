package me.ninni.macaws.entity.access;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public interface HeadMountAccess {
    TrackedData<NbtCompound> HEAD_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

    NbtCompound getHeadEntity();
    void setHeadEntity(NbtCompound nbt);
    boolean addHeadEntity(NbtCompound nbt);

    void tryDropHeadEntity();
    void dropHeadEntity(NbtCompound nbt);

    boolean disallowHeadMount();
}
