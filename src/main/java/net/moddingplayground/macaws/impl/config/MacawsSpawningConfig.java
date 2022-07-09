package net.moddingplayground.macaws.impl.config;

import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.config.v0.Config;
import net.moddingplayground.frame.api.config.v0.option.IntOption;
import net.moddingplayground.frame.api.config.v0.option.Option;
import net.moddingplayground.macaws.api.Macaws;

import java.io.File;

public class MacawsSpawningConfig extends Config {
    public static final MacawsSpawningConfig INSTANCE = new MacawsSpawningConfig(createFile("moddingplayground/%s_spawning".formatted(Macaws.MOD_ID))).load();

    public final IntOption weight = macaw("weight", IntOption.ofMin(50, 0));
    public final IntOption minGroupSize = macaw("min_group_size", IntOption.ofMin(1, 1));
    public final IntOption maxGroupSize = macaw("max_group_size", IntOption.ofMin(2, 1));

    public MacawsSpawningConfig(File file) {
        super(file);
    }

    private <T, O extends Option<T>> O macaw(String id, O option) {
        return this.add(new Identifier("macaw", id), option);
    }
}
