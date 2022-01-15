package net.moddingplayground.macaws;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.moddingplayground.macaws.entity.MacawsEntities;
import net.moddingplayground.macaws.sound.MacawsSoundEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Macaws implements ModInitializer {
    public static final String MOD_ID   = "macaws";
    public static final String MOD_NAME = "Macaws";
    public static final Logger LOGGER   = LogManager.getLogger(MOD_ID);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}", MOD_NAME);

        Reflection.initialize(
            MacawsSoundEvents.class,
            MacawsEntities.class
        );

        LOGGER.info("Initialized {}", MOD_NAME);
    }
}
