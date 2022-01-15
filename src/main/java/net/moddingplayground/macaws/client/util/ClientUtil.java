package net.moddingplayground.macaws.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.Macaws;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    public static Identifier texture(String s) {
        return new Identifier(Macaws.MOD_ID, "textures/%s.png".formatted(s));
    }

    public static Identifier entityTexture(String s) {
        return texture("entity/%s".formatted(s));
    }
}
