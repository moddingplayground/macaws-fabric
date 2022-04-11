package net.moddingplayground.macaws.impl.data;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.toymaker.v0.generator.loot.AbstractEntityTypeLootTableGenerator;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;

public class EntityTypeLootGenerator extends AbstractEntityTypeLootTableGenerator {
    public EntityTypeLootGenerator() {
        super(Macaws.MOD_ID);
    }

    @Override
    public void generate() {
        this.add(MacawsEntityType.MACAW,
            LootTable.builder()
                     .pool(pool(count(1))
                         .with(ItemEntry.builder(Items.FEATHER).apply(setCount(countRandom(1, 2))))
                         .apply(LootingEnchantLootFunction.builder(countRandom(0, 1)))
                     )
        );
    }

    @Override
    public void testObject(Identifier id, EntityType<?> obj) {}
}
