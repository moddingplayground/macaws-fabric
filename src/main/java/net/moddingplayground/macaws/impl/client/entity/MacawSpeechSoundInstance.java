package net.moddingplayground.macaws.impl.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class MacawSpeechSoundInstance extends EntityTrackingSoundInstance {
    private int tick;

    public MacawSpeechSoundInstance(SoundEvent sound, float pitch, Entity entity, long seed) {
        super(sound, SoundCategory.NEUTRAL, 1.0F, pitch, entity, seed);
    }

    @Override
    public void tick() {
        super.tick();
        this.tick++;
        if (this.tick > 13) this.setDone();
    }
}
