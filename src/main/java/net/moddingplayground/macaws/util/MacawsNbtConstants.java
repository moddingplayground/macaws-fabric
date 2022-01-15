package net.moddingplayground.macaws.util;

import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.Macaws;

public class MacawsNbtConstants {
    public static final String NBT_MACAW_VARIANT = create("macaw_variant");
    public static final String NBT_HAS_EYEPATCH = create("has_eyepatch");
    public static final String NBT_RING_COLOR = create("ring_color");
    public static final String NBT_HEAD_ENTITY = create("head_entity");

    public static final String NBT_PERSONALITY = create("macaw_personality");
    public static final String NBT_PERSONALITY_PITCH = "pitch";

    public static final String NBT_SILENT = "Silent";

    private static String create(String id) {
        return new Identifier(Macaws.MOD_ID, id).toString();
    }
}
