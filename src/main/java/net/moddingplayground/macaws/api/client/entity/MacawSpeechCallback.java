package net.moddingplayground.macaws.api.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.entity.MacawEntity;

/**
 * Called when the client receives a Macaw speech packet.
 */
@FunctionalInterface
public interface MacawSpeechCallback {
    Identifier PACKET_ID = new Identifier(Macaws.MOD_ID, "macaw_speech");

    @Environment(EnvType.CLIENT)
    Event<MacawSpeechCallback> EVENT = EventFactory.createArrayBacked(
        MacawSpeechCallback.class,
        callbacks -> (player, source, personality, stack) -> { for (MacawSpeechCallback callback : callbacks) callback.onSpeech(player, source, personality, stack); }
    );

    @Environment(EnvType.CLIENT)
    void onSpeech(PlayerEntity player, PlayerEntity source, MacawEntity.Personality personality, ItemStack stack);
}
