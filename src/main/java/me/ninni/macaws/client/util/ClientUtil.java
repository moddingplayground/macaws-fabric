package me.ninni.macaws.client.util;

import me.ninni.macaws.Macaws;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    public static Identifier texture(String s) {
        return new Identifier(Macaws.MOD_ID, "textures/%s.png".formatted(s));
    }

    public static Identifier entityTexture(String s) {
        return texture("entity/%s".formatted(s));
    }
}
