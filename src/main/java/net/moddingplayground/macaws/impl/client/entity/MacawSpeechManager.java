package net.moddingplayground.macaws.impl.client.entity;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.client.entity.MacawSpeechCallback;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import static net.moddingplayground.macaws.api.entity.MacawEntity.*;

@Environment(EnvType.CLIENT)
public class MacawSpeechManager implements Macaws, MacawSpeechCallback {
    private static final SoundEvent EMPTY = new SoundEvent(new Identifier(""));

    private final Map<Item, SoundEvent> cache = Maps.newHashMap();
    private @Nullable EntityTrackingSoundInstance lastInstance;

    public MacawSpeechManager() {}

    @Override
    public void onSpeech(PlayerEntity player, PlayerEntity source, Personality personality, ItemStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        SoundManager soundManager = client.getSoundManager();

        if (soundManager.isPlaying(this.lastInstance)) return;
        if (source.distanceTo(player) > 16.0F) return;

        Item item = stack.getItem();
        Optional.ofNullable(this.cache.computeIfAbsent(item, this::createSound)).ifPresent(sound -> {
            if (sound == EMPTY) return;
            this.lastInstance = new MacawSpeechSoundInstance(sound, personality.pitch(), source, player.getRandom().nextLong());
            soundManager.play(this.lastInstance);
        });
    }

    public SoundEvent createSound(Item item) {
        String suffix = Registry.ITEM.getId(item).toUnderscoreSeparatedString();
        Identifier id = new Identifier(Macaws.MOD_ID, "entity.macaw.speech.item.%s".formatted(suffix));

        MinecraftClient client = MinecraftClient.getInstance();
        SoundManager soundManager = client.getSoundManager();
        return !soundManager.getKeys().contains(id) ? EMPTY : new SoundEvent(id); // prevent log spam by verifying the sound's existence
    }

    public void reload() {
        this.cache.clear();
        this.lastInstance = null;
        LOGGER.info("[{}] Reloaded speech manager", MOD_NAME);
    }
}
