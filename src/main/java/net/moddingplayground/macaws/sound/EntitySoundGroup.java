package net.moddingplayground.macaws.sound;

import net.minecraft.sound.SoundEvent;

import static net.moddingplayground.macaws.sound.MacawsSoundEvents.*;

public enum EntitySoundGroup {
    MACAW(ENTITY_MACAW_AMBIENT, ENTITY_MACAW_HURT, ENTITY_MACAW_DEATH),
    MACAW_TAMED(ENTITY_MACAW_AMBIENT_TAMED, ENTITY_MACAW_HURT_TAMED, ENTITY_MACAW_DEATH_TAMED);

    private final SoundEvent ambient, hurt, death;

    EntitySoundGroup(SoundEvent ambient, SoundEvent hurt, SoundEvent death) {
        this.ambient = ambient;
        this.hurt = hurt;
        this.death = death;
    }

    public SoundEvent getAmbient() {
        return this.ambient;
    }

    public SoundEvent getHurt() {
        return this.hurt;
    }

    public SoundEvent getDeath() {
        return this.death;
    }
}
