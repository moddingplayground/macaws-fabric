package net.moddingplayground.macaws.entity.data;

import net.minecraft.entity.data.TrackedDataHandler;
import net.moddingplayground.macaws.entity.MacawEntity;
import net.moddingplayground.macaws.mixin.TrackedDataHandlerRegistryAccessor;

import java.util.ArrayList;

public class MacawsTrackedDataHandlerRegistry {
    private static final ArrayList<TrackedDataHandler<?>> HANDLERS = new ArrayList<>();

    public static final TrackedDataHandler<MacawEntity.Variant> MACAW_VARIANT = register(new EnumTrackedDataHandler<>(MacawEntity.Variant.class));
    public static final TrackedDataHandler<MacawEntity.Personality> MACAW_PERSONALITY = register(new PackagerTrackedDataHandler<>(() -> MacawEntity.Personality.DEFAULT));

    static {
        HANDLERS.forEach(TrackedDataHandlerRegistryAccessor.getDATA_HANDLERS()::add);
    }

    public static <T> TrackedDataHandler<T> register(TrackedDataHandler<T> handler) {
        HANDLERS.add(handler);
        return handler;
    }
}
