package me.ninni.macaws.entity;

import me.ninni.macaws.Macaws;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

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
