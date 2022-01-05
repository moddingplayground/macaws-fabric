package me.ninni.macaws.util;

import me.ninni.macaws.Macaws;
import net.minecraft.util.Identifier;

public class MacawsNbtConstants {
    public static final String NBT_MACAW_VARIANT = create("macaw_variant");
    public static final String NBT_HAS_EYEPATCH = create("has_eyepatch");

    private static String create(String id) {
        return new Identifier(Macaws.MOD_ID, id).toString();
    }
}
