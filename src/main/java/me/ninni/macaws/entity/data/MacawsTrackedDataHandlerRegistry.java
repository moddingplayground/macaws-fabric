package me.ninni.macaws.entity.data;

import me.ninni.macaws.mixin.TrackedDataHandlerRegistryAccessor;
import net.minecraft.entity.data.TrackedDataHandler;

import java.util.ArrayList;

import static me.ninni.macaws.entity.macaw.MacawEntity.*;

public class MacawsTrackedDataHandlerRegistry {
    private static final ArrayList<TrackedDataHandler<?>> HANDLERS = new ArrayList<>();

    public static final TrackedDataHandler<Variant> MACAW_VARIANT = register(new EnumTrackedDataHandler<>(Variant.class));
    public static final TrackedDataHandler<Personality> MACAW_PERSONALITY = register(new PackagerTrackedDataHandler<>(() -> Personality.EMPTY));

    static {
        HANDLERS.forEach(TrackedDataHandlerRegistryAccessor.getDATA_HANDLERS()::add);
    }

    public static <T> TrackedDataHandler<T> register(TrackedDataHandler<T> handler) {
        HANDLERS.add(handler);
        return handler;
    }
}
