package net.moddingplayground.macaws.impl.config;

import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.config.v0.Config;
import net.moddingplayground.frame.api.config.v0.option.IntOption;
import net.moddingplayground.frame.api.config.v0.option.Option;
import net.moddingplayground.macaws.api.Macaws;

import java.io.File;

public class MacawsConfig extends Config {
    public static final MacawsConfig INSTANCE = new MacawsConfig(createFile("moddingplayground/%s".formatted(Macaws.MOD_ID))).load();

    public final IntOption weight = add("entity_macaw_spawn_weight", IntOption.ofMin(50, 0));
    public final IntOption minGroupSize = add("entity_macaw_spawn_min_group_size", IntOption.ofMin(1, 1));
    public final IntOption maxGroupSize = add("entity_macaw_spawn_max_group_size", IntOption.ofMin(2, 1));

    public MacawsConfig(File file) {
        super(file);
    }

    private <T, O extends Option<T>> O add(String id, O option) {
        return this.add(new Identifier(Macaws.MOD_ID, id), option);
    }
}
