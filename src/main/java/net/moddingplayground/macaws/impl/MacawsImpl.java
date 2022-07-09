package net.moddingplayground.macaws.impl;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.entity.MacawsEntityType;
import net.moddingplayground.macaws.api.sound.MacawsSoundEvents;
import net.moddingplayground.macaws.impl.config.MacawsSpawningConfig;

public class MacawsImpl implements Macaws, ModInitializer {
    private static MacawsImpl instance;
    private final InitializationLogger initializer;

    public MacawsImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME);
        instance = this;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        this.initializer.start();
        Reflection.initialize(MacawsSpawningConfig.class, MacawsSoundEvents.class, MacawsEntityType.class);
        this.initializer.finish();
    }

    public static MacawsImpl getInstance() {
        return instance;
    }
}
