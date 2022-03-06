package net.moddingplayground.macaws.impl.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.macaws.api.Macaws;

@Environment(EnvType.CLIENT)
public final class MacawsClientImpl implements Macaws, ClientModInitializer {
    private static MacawsClientImpl instance;
    private final InitializationLogger initializer;

    public MacawsClientImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME, EnvType.CLIENT);
        instance = this;
    }

    @Override
    public void onInitializeClient() {
        this.initializer.start();

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
