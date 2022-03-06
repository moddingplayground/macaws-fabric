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
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface MacawSpeechCallback {
    Event<MacawSpeechCallback> EVENT = EventFactory.createArrayBacked(
        MacawSpeechCallback.class,
        callbacks -> (player, source, personality, stack) -> { for (MacawSpeechCallback callback : callbacks) callback.onSpeech(player, source, personality, stack); }
    );

    Identifier PACKET_ID = new Identifier(Macaws.MOD_ID, "macaw_speech");

    void onSpeech(PlayerEntity player, PlayerEntity source, MacawEntity.Personality personality, ItemStack stack);
}
