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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "onPlayerCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onOnPlayerCollisionAsInsertStack(PlayerEntity player, CallbackInfo ci, ItemStack stack) {
        PlayerInventory inventory = player.getInventory();
        if (!inventory.contains(stack)) {
            HeadMountAccess access = (HeadMountAccess) inventory.player;
            NbtCompound nbt = access.getHeadEntity();
            if (!nbt.isEmpty()) access.onNovelItemPickUp(stack);
        }
    }
}
