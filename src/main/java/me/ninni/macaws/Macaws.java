package me.ninni.macaws;

import com.google.common.reflect.Reflection;
import me.ninni.macaws.entity.MacawsEntities;
import me.ninni.macaws.entity.access.HeadMountAccess;
import me.ninni.macaws.sound.MacawsSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Macaws implements ModInitializer {
    public static final String MOD_ID   = "macaws";
    public static final String MOD_NAME = "Macaws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {}", MOD_NAME);

        Reflection.initialize(
            MacawsSoundEvents.class,
            HeadMountAccess.class,
            MacawsEntities.class
        );

        LOGGER.info("Initialized {}", MOD_NAME);
    }
}
