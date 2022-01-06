package me.ninni.macaws;

import com.google.common.reflect.Reflection;
import me.ninni.macaws.entity.MacawsEntities;
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

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(SpawnEggItem.forEntity(MacawsEntities.MACAW))
    );

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
