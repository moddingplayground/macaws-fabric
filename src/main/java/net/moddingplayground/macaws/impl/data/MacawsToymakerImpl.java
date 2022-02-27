package net.moddingplayground.macaws.impl.data;

import net.minecraft.loot.context.LootContextTypes;
import net.moddingplayground.frame.api.toymaker.v0.ToymakerEntrypoint;
import net.moddingplayground.frame.api.toymaker.v0.registry.generator.ItemModelGeneratorStore;
import net.moddingplayground.frame.api.toymaker.v0.registry.generator.LootGeneratorStore;
import net.moddingplayground.frame.api.toymaker.v0.registry.generator.TagGeneratorStore;
import net.moddingplayground.macaws.api.Macaws;

public class MacawsToymakerImpl implements Macaws, ToymakerEntrypoint {
    @Override
    public void onInitializeToymaker() {
        TagGeneratorStore.register(() -> new BiomeTagGenerator(MOD_ID));
        ItemModelGeneratorStore.register(() -> new ItemModelGenerator(MOD_ID));
        LootGeneratorStore.register(() -> new EntityTypeLootGenerator(MOD_ID), LootContextTypes.ENTITY);
    }
}
