package net.moddingplayground.macaws.impl.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.impl.client.config.MacawsConfigScreenFactory;

@Environment(EnvType.CLIENT)
public final class MacawsModMenuImpl implements Macaws, ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new MacawsConfigScreenFactory(parent).create();
    }
}
