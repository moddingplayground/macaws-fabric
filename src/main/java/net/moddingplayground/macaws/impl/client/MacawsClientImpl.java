package net.moddingplayground.macaws.impl.client;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;
import net.moddingplayground.macaws.impl.client.model.MacawEntityModelLayers;
import net.moddingplayground.macaws.impl.client.render.entity.MacawEntityRenderer;

@Environment(EnvType.CLIENT)
public final class MacawsClientImpl implements Macaws, ClientModInitializer {
    private static MacawsClientImpl instance;
    private final InitializationLogger initializer;

    public MacawsClientImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME, EnvType.CLIENT);
        instance = this;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        this.initializer.start();

        Reflection.initialize(MacawEntityModelLayers.class);
        EntityRendererRegistry.register(MacawsEntityType.MACAW, MacawEntityRenderer::new);

        // register parrot reskin resource pack
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "parakeets"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });

        this.initializer.finish();
    }

    public static MacawsClientImpl getInstance() {
        return instance;
    }
}
