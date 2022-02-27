package net.moddingplayground.macaws.impl.data;

import net.minecraft.item.SpawnEggItem;
import net.moddingplayground.frame.api.toymaker.v0.generator.model.item.AbstractItemModelGenerator;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;

public class ItemModelGenerator extends AbstractItemModelGenerator {
    public ItemModelGenerator(String modId) {
        super(modId);
    }

    @Override
    public void generate() {
        this.add(SpawnEggItem.forEntity(MacawsEntityType.MACAW), i -> this.spawnEgg());
    }
}
