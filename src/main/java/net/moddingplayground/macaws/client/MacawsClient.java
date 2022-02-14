package net.moddingplayground.macaws.client;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.Macaws;
import net.moddingplayground.macaws.client.model.MacawEntityModelLayers;
import net.moddingplayground.macaws.client.render.entity.MacawEntityRenderer;
import net.moddingplayground.macaws.entity.MacawsEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class MacawsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("%s-client".formatted(Macaws.MOD_ID));

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {}-client", Macaws.MOD_NAME);

        Reflection.initialize(MacawEntityModelLayers.class);
        EntityRendererRegistry.register(MacawsEntities.MACAW, MacawEntityRenderer::new);

        // register parrot reskin resource pack
        FabricLoader.getInstance().getModContainer(Macaws.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(Macaws.MOD_ID, "parakeets"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });
    }
}
