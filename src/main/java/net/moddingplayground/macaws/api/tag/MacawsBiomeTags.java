package net.moddingplayground.macaws.api.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.moddingplayground.macaws.api.Macaws;

public interface MacawsBiomeTags {
    Tag.Identified<Biome> SPAWNS_MACAW = register("spawns_macaw");

    private static Tag.Identified<Biome> register(String id) {
        return TagFactory.BIOME.create(new Identifier(Macaws.MOD_ID, id));
    }
}
