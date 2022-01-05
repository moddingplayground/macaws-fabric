package me.ninni.macaws.entity.macaw;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Random;
import java.util.function.Function;

import static me.ninni.macaws.client.util.ClientUtil.*;
import static me.ninni.macaws.util.MacawsNbtConstants.*;

public enum MacawVariant {
    BLUE_AND_GOLD, SCARLET, HYACINTH, GREEN;

    @Environment(EnvType.CLIENT)
    private static final Function<String, Identifier> ID_TO_TEXTURE = Util.memoize(id -> entityTexture("macaw/macaw_%s".formatted(id)));

    private static final MacawVariant[] VALUES = MacawVariant.values();
    public static final MacawVariant DEFAULT = VALUES[0];

    private final String id;

    MacawVariant() {
        this.id = this.name().toLowerCase();
    }

    public String getId() {
        return this.id;
    }

    public Identifier getTexture() {
        return ID_TO_TEXTURE.apply(this.getId());
    }

    public void writeToNbt(NbtCompound nbt) {
        nbt.putString(NBT_MACAW_VARIANT, this.name());
    }

    public static MacawVariant readFromNbt(NbtCompound nbt) {
        return MacawVariant.safeValueOf(nbt.getString(NBT_MACAW_VARIANT));
    }

    public static MacawVariant random(Random random) {
        return VALUES[random.nextInt(VALUES.length)];
    }

    public static MacawVariant safeValueOf(String name) {
        try { return MacawVariant.valueOf(name); } catch (IllegalArgumentException ignored) {}
        return DEFAULT;
    }
}
