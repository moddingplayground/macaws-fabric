package me.ninni.macaws.entity.data;

import me.ninni.macaws.entity.macaw.MacawVariant;
import me.ninni.macaws.mixin.TrackedDataHandlerRegistryAccessor;
import net.minecraft.entity.data.TrackedDataHandler;

import java.util.ArrayList;

public class MacawsTrackedDataHandlerRegistry {
    private static final ArrayList<TrackedDataHandler<?>> HANDLERS = new ArrayList<>();

    public static final TrackedDataHandler<MacawVariant> MACAW_VARIANT = register(new EnumTrackedDataHandler<>(MacawVariant.class));

    static {
        HANDLERS.forEach(TrackedDataHandlerRegistryAccessor.getDATA_HANDLERS()::add);
    }

    public static <T> TrackedDataHandler<T> register(TrackedDataHandler<T> handler) {
        HANDLERS.add(handler);
        return handler;
    }
}
