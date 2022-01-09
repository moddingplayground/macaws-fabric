package me.ninni.macaws.client;

import com.google.common.reflect.Reflection;
import me.ninni.macaws.Macaws;
import me.ninni.macaws.client.model.MacawEntityModelLayers;
import me.ninni.macaws.client.render.entity.MacawEntityRenderer;
import me.ninni.macaws.entity.MacawsEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static me.ninni.macaws.Macaws.*;

@Environment(EnvType.CLIENT)
public class MacawsClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("%s-client".formatted(MOD_ID));

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {}-client", Macaws.MOD_NAME);

        Reflection.initialize(MacawEntityModelLayers.class);
        EntityRendererRegistry.register(MacawsEntities.MACAW, MacawEntityRenderer::new);

        // register parrot reskin resource pack
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "parakeets"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });

        LOGGER.info("Initialized {}-client", Macaws.MOD_NAME);
    }
}
