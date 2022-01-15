package net.moddingplayground.macaws.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.moddingplayground.macaws.Macaws;

public class MacawsSoundEvents {
    public static final SoundEvent ENTITY_MACAW_AMBIENT = macaw("ambient");
    public static final SoundEvent ENTITY_MACAW_HURT = macaw("hurt");
    public static final SoundEvent ENTITY_MACAW_DEATH = macaw("death");

    public static final SoundEvent ENTITY_MACAW_AMBIENT_TAMED = macaw("ambient_tamed");
    public static final SoundEvent ENTITY_MACAW_HURT_TAMED = macaw("hurt_tamed");
    public static final SoundEvent ENTITY_MACAW_DEATH_TAMED = macaw("death_tamed");

    public static final SoundEvent ENTITY_MACAW_AMBIENT_EYEPATCH = macaw("ambient_eyepatch");

    public static final SoundEvent ENTITY_MACAW_EAT = macaw("eat");
    public static final SoundEvent ENTITY_MACAW_FLY = macaw("fly");
    public static final SoundEvent ENTITY_MACAW_SHEAR = macaw("shear");
    public static final SoundEvent ENTITY_MACAW_EQUIP_EYEPATCH = macaw("equip_eyepatch");

    public static final SoundEvent ENTITY_MACAW_MOUNT_ON = macaw("mount_on");
    public static final SoundEvent ENTITY_MACAW_MOUNT_OFF = macaw("mount_off");

    private static SoundEvent macaw(String id) {
        return entity("macaw", id);
    }

    private static SoundEvent entity(String entity, String id) {
        return register("entity." + entity + "." + id);
    }

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(Macaws.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}