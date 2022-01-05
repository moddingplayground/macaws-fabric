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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class MacawsClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("%s-client".formatted(Macaws.MOD_ID));

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {}-client", Macaws.MOD_NAME);

        Reflection.initialize(MacawEntityModelLayers.class);
        EntityRendererRegistry.register(MacawsEntities.MACAW, MacawEntityRenderer::new);

        LOGGER.info("Initialized {}-client", Macaws.MOD_NAME);
    }
}
