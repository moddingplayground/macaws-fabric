package net.moddingplayground.macaws.impl.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;

import java.util.function.BiConsumer;

public final class EntityTypeLootTableProvider extends SimpleFabricLootTableProvider {
    public EntityTypeLootTableProvider(FabricDataGenerator gen) {
        super(gen, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(MacawsEntityType.MACAW.getLootTableId(),
            LootTable.builder()
                     .pool(
                         LootPool.builder()
                                 .rolls(ConstantLootNumberProvider.create(1))
                                 .with(
                                     ItemEntry.builder(Items.FEATHER)
                                              .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2)))
                                 )
                                 .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0, 1)))
                     )
        );
    }
}
