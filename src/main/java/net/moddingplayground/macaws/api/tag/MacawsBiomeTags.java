package net.moddingplayground.macaws.api.tag;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.moddingplayground.macaws.api.Macaws;

public interface MacawsBiomeTags {
    TagKey<Biome> SPAWNS_MACAW = register("spawns_macaw");

    private static TagKey<Biome> register(String id) {
        return TagKey.of(Registry.BIOME_KEY, new Identifier(Macaws.MOD_ID, id));
    }
}
