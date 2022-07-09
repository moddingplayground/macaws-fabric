package net.moddingplayground.macaws.impl.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.moddingplayground.macaws.api.Macaws;

public final class MacawsDataGeneratorImpl implements Macaws, DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        gen.addProvider(BiomeTagGenerator::new);
        gen.addProvider(ModelProvider::new);
        gen.addProvider(EntityTypeLootTableProvider::new);
    }
}
