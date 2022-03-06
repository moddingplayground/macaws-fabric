package net.moddingplayground.macaws.impl.client.entity;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.client.entity.MacawSpeechCallback;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;
import net.moddingplayground.macaws.impl.client.model.MacawEntityModelLayers;
import net.moddingplayground.macaws.impl.client.render.entity.MacawEntityRenderer;

import java.util.Optional;
import java.util.UUID;

import static net.moddingplayground.macaws.api.entity.MacawEntity.*;

@Environment(EnvType.CLIENT)
public class MacawsEntityTypeClientImpl implements MacawsEntityType, ClientModInitializer {
    private final MacawSpeechManager speechManager = new MacawSpeechManager();

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        Reflection.initialize(MacawEntityModelLayers.class);
        EntityRendererRegistry.register(MACAW, MacawEntityRenderer::new);

        // listen for speech packets
        ClientPlayNetworking.registerGlobalReceiver(MacawSpeechCallback.PACKET_ID, (client, handler, buf, sender) -> {
            UUID uuid = buf.readUuid();
            Optional.ofNullable(client.world.getPlayerByUuid(uuid)).ifPresent(source -> {
                NbtCompound personalityNbt = buf.readNbt();
                ItemStack stack = buf.readItemStack();
                Personality personality = Personality.readFromNbt(personalityNbt);
                MacawSpeechCallback.EVENT.invoker().onSpeech(client.player, source, personality, stack);
            });
        });

        MacawSpeechCallback.EVENT.register(this.speechManager);

        // reload speech manager on resource reload
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            public static final Identifier LISTENER_ID = new Identifier(MOD_ID, "reload/client_resources");

            @Override
            public Identifier getFabricId() {
                return LISTENER_ID;
            }

            @Override
            public void reload(ResourceManager manager) {
                MacawsEntityTypeClientImpl.this.speechManager.reload();
            }
        });
    }
}
