package net.moddingplayground.macaws.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.moddingplayground.macaws.entity.MacawEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public class EntityTypeMixin {
    // cancel ambient sound on spawn if macaw
    @Redirect(method = "create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;playAmbientSound()V"))
    private void onCreate(MobEntity entity) {
        if (entity instanceof MacawEntity && entity.isSilent()) return;
        entity.playAmbientSound();
    }
}
