package me.ninni.macaws.entity;

import me.ninni.macaws.Macaws;
import me.ninni.macaws.entity.access.HeadMountAccess;
import me.ninni.macaws.sound.MacawsSoundEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

import static me.ninni.macaws.util.MacawsNbtConstants.*;

public class MacawsEntities {
    public static final EntityType<MacawEntity> MACAW = register(
        "macaw",
        FabricEntityTypeBuilder.createMob()
                               .entityFactory(MacawEntity::new).spawnGroup(SpawnGroup.CREATURE)
                               .dimensions(EntityDimensions.fixed(0.5F, 0.9F))
                               .defaultAttributes(
                                   () -> MobEntity.createMobAttributes()
                                                  .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                                                  .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4D)
                                                  .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                               )
                               .trackRangeChunks(8),
        colors(0x2864C7, 0xFBCA0C)
    );

    static {
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack stack = player.getStackInHand(hand);
            HeadMountAccess access = (HeadMountAccess) player;

            if (world instanceof ServerWorld serverWorld) {
                if (!access.getHeadEntity().isEmpty() && stack.isEmpty() && player.isSneaking()) {
                    MacawEntity dummy = MacawsEntities.MACAW.create(serverWorld, createSilentNbt(), null, null, hit.getBlockPos().offset(hit.getSide()), SpawnReason.TRIGGERED, true, false);
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
    }

    private static NbtCompound createSilentNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putBoolean(NBT_SILENT, true);
        return nbt;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, Pair<Integer, Integer> colors, SpawnEggFactory eggFactory) {
        EntityType<T> builtEntityType = entityType.build();
        if (eggFactory != null) {
            Item.Settings settings = new FabricItemSettings().maxCount(64).group(Macaws.ITEM_GROUP);
            Item item = eggFactory.apply((EntityType<? extends MobEntity>) builtEntityType, colors.getLeft(), colors.getRight(), settings);
            Registry.register(Registry.ITEM,  new Identifier(Macaws.MOD_ID, "%s_spawn_egg".formatted(id)), item);
        }
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Macaws.MOD_ID, id), builtEntityType);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, Pair<Integer, Integer> colors) {
        return register(id, entityType, colors, SpawnEggItem::new);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return register(id, entityType, null, null);
    }

    private static Pair<Integer, Integer> colors(int primary, int secondary) {
        return new Pair<>(primary, secondary);
    }

    @FunctionalInterface
    private interface SpawnEggFactory {
        SpawnEggItem apply(EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Item.Settings settings);
    }
}
