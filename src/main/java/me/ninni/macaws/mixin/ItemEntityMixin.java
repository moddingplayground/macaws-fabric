package me.ninni.macaws.mixin;

import me.ninni.macaws.entity.access.HeadMountAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean onOnPlayerCollisionAsInsertStack(PlayerInventory inventory, ItemStack stack) {
        if (!inventory.contains(stack)) {
            HeadMountAccess access = (HeadMountAccess) inventory.player;
            NbtCompound nbt = access.getHeadEntity();
            if (!nbt.isEmpty()) access.onNovelItemPickUp(stack);
        }

        return inventory.insertStack(stack);
    }
}
