package net.moddingplayground.macaws.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    // prevent log-spam from frequent missing sound events to do with macaw speech
    @Redirect(method = "play(Lnet/minecraft/cli ent/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Object;)V"))
    private void onWarn(Logger logger, Marker marker, String s, Object o) {
        if (o instanceof Identifier id && id.getNamespace().equals(Macaws.MOD_ID) && id.getPath().startsWith("entity.macaw.speech")) return;
        logger.warn(marker, s, o);
    }
}
