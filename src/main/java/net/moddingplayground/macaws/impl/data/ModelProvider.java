package net.moddingplayground.macaws.impl.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.macaws.api.Macaws;

import java.util.stream.StreamSupport;

public final class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataGenerator gen) {
        super(gen);
    }

    private boolean isMacaws(Item item) {
        Identifier id = Registry.ITEM.getId(item);
        return id.getNamespace().equals(Macaws.MOD_ID);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        StreamSupport.stream(SpawnEggItem.getAll().spliterator(), false)
                     .filter(this::isMacaws)
                     .forEach(item -> gen.registerParentedItemModel(item, ModelIds.getMinecraftNamespacedItem("template_spawn_egg")));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
    }
}
