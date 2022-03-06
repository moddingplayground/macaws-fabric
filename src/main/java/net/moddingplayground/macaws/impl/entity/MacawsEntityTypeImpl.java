package net.moddingplayground.macaws.impl.entity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import net.moddingplayground.frame.api.config.v0.option.IntOption;
import net.moddingplayground.macaws.api.entity.MacawEntity;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;
import net.moddingplayground.macaws.api.sound.MacawsSoundEvents;
import net.moddingplayground.macaws.api.tag.MacawsBiomeTags;
import net.moddingplayground.macaws.impl.config.MacawsConfig;

import static net.moddingplayground.macaws.api.util.MacawsNbtConstants.*;

public final class MacawsEntityTypeImpl implements MacawsEntityType, ModInitializer {
    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack stack = player.getStackInHand(hand);
            HeadMountAccess access = (HeadMountAccess) player;

            if (world instanceof ServerWorld serverWorld) {
                if (!access.getHeadEntity().isEmpty() && stack.isEmpty() && player.isSneaking()) {
                    MacawEntity dummy = MacawsEntityType.MACAW.create(serverWorld, createSilentNbt(), null, null, hit.getBlockPos().offset(hit.getSide()), SpawnReason.TRIGGERED, true, false);
                    if (dummy != null) {
                        Vec3d pos = dummy.getPos();
                        BlockPos bpos = new BlockPos(pos);
                        if (world.getBlockState(bpos).getCollisionShape(world, bpos).isEmpty()) {
                            if (access.tryDropHeadEntity(pos)) {
                                world.emitGameEvent(GameEvent.ENTITY_PLACE, player);
                                player.world.playSound(null, player.getX(), player.getY(), player.getZ(), MacawsSoundEvents.ENTITY_MACAW_MOUNT_OFF, player.getSoundCategory(), 0.3f, 0.3f + (player.getRandom().nextFloat() * 0.2f));
                                return ActionResult.SUCCESS;
                            }
                        }
                    }
                }
            }

            return ActionResult.PASS;
        });

        MacawsConfig config = MacawsConfig.INSTANCE;
        addSpawn(MacawsBiomeTags.SPAWNS_MACAW, MACAW, config.weight, config.minGroupSize, config.maxGroupSize);
    }

    public void addSpawn(TagKey<Biome> tag, EntityType<?> entity, IntOption weight, IntOption minSize, IntOption maxSize) {
        BiomeModifications.addSpawn(BiomeSelectors.tag(tag), entity.getSpawnGroup(), entity, weight.getValue(), minSize.getValue(), maxSize.getValue());
    }

    public NbtCompound createSilentNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean(NBT_SILENT, true);
        return nbt;
    }
}
