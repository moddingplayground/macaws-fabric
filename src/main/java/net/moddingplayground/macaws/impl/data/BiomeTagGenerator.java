package net.moddingplayground.macaws.impl.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.moddingplayground.macaws.api.tag.MacawsBiomeTags;

import static net.minecraft.world.biome.BiomeKeys.*;

public final class BiomeTagGenerator extends FabricTagProvider.DynamicRegistryTagProvider<Biome> {
    public BiomeTagGenerator(FabricDataGenerator gen) {
        super(gen, Registry.BIOME_KEY);
    }

    @Override
    public void generateTags() {
        this.getOrCreateTagBuilder(MacawsBiomeTags.SPAWNS_MACAW).add(
            JUNGLE,
            SPARSE_JUNGLE,
            BAMBOO_JUNGLE
        );
    }
}
