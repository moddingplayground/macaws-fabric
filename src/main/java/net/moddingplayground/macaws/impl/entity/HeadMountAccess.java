package net.moddingplayground.macaws.impl.entity;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.moddingplayground.macaws.api.entity.TameableHeadEntity;
import org.jetbrains.annotations.Nullable;

public interface HeadMountAccess {
    TrackedData<NbtCompound> HEAD_ENTITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    NbtCompound getHeadEntity();
    void setHeadEntity(NbtCompound nbt);
    boolean addHeadEntity(NbtCompound nbt);

    boolean tryDropHeadEntity(Vec3d pos);
    boolean tryDropHeadEntity();
    void dropHeadEntity(NbtCompound nbt, Vec3d pos);

    boolean canHeadMount(@Nullable TameableHeadEntity entity);
    boolean canHeadDismount();

    /**
     * Runs when the player picks up an item that was not in their inventory before.
     * Does not run client-side.
     */
    void onNovelItemPickUp(ItemStack stack);
}
