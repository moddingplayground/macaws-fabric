package net.moddingplayground.macaws.impl.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.impl.config.MacawsSpawningConfig;

@Environment(EnvType.CLIENT)
public class MacawsConfigScreenFactory {
    private final Screen parent;

    public MacawsConfigScreenFactory(Screen parent) {
        this.parent = parent;
    }

    public Screen create() {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setParentScreen(this.parent)
                                             .setDefaultBackgroundTexture(new Identifier("textures/block/jungle_log.png"))
                                             .setTitle(text("title"))
                                             .setSavingRunnable(this::save);
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(false);

        ConfigEntryBuilder entries = builder.entryBuilder();
        MacawsSpawningConfig.INSTANCE.addConfigListEntries(entries, () -> builder.getOrCreateCategory(text("category.spawning")));

        return builder.build();
    }

    public void save() {
        MacawsSpawningConfig.INSTANCE.save();

        MinecraftClient client = MinecraftClient.getInstance();
        ToastManager toasts = client.getToastManager();
        SystemToast.add(
            toasts, SystemToast.Type.TUTORIAL_HINT,
            Text.translatable("config.macaws.save_toast_line0"),
            Text.translatable("config.macaws.save_toast_line1").setStyle(Style.EMPTY.withColor(0xFCFC00))
        );
    }

    public Text text(String label) {
        return Text.translatable("config.%s.%s".formatted(Macaws.MOD_ID, label));
    }
}
