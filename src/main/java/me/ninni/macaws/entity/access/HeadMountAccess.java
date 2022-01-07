package me.ninni.macaws.entity.access;

import net.minecraft.nbt.NbtCompound;

public interface HeadMountAccess {
    NbtCompound getHeadEntity();
    void setHeadEntity(NbtCompound nbt);
    boolean addHeadEntity(NbtCompound nbt);

    void tryDropHeadEntity();
    void dropHeadEntity(NbtCompound nbt);

    boolean disallowHeadMount();
}
