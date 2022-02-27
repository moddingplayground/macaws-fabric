package net.moddingplayground.macaws.api.util;

import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;

public interface MacawsNbtConstants {
    String NBT_MACAW_VARIANT = create("macaw_variant");
    String NBT_HAS_EYEPATCH = create("has_eyepatch");
    String NBT_RING_COLOR = create("ring_color");
    String NBT_HEAD_ENTITY = create("head_entity");

    String NBT_PERSONALITY = create("macaw_personality");
    String NBT_PERSONALITY_PITCH = "pitch";

    String NBT_SILENT = "Silent";

    private static String create(String id) {
        return new Identifier(Macaws.MOD_ID, id).toString();
    }
}
