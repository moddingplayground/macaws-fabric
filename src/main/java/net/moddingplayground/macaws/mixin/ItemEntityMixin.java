package net.moddingplayground.macaws.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.moddingplayground.macaws.impl.entity.HeadMountAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    private @Unique ItemStack macaws_preInsertStack = ItemStack.EMPTY;

    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "onPlayerCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getCount()I"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void beforeOnPlayerCollisionInsertStack(PlayerEntity player, CallbackInfo ci, ItemStack stack) {
        PlayerInventory inventory = player.getInventory();
        this.macaws_preInsertStack = inventory.contains(stack) ? ItemStack.EMPTY : stack.copy();
    }

    @Inject(
        method = "onPlayerCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V",
            shift = At.Shift.AFTER
        )
    )
    private void afterOnPlayerCollisionInsertStack(PlayerEntity player, CallbackInfo ci) {
        ItemStack stack = this.macaws_preInsertStack;
        if (!stack.isEmpty()) {
            HeadMountAccess access = (HeadMountAccess) player;
            NbtCompound nbt = access.getHeadEntity();
            if (!nbt.isEmpty()) access.onNovelItemPickUp(stack);
        }
    }
}
