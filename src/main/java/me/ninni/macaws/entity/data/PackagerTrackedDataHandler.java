package me.ninni.macaws.entity.data;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

public record PackagerTrackedDataHandler<T extends TrackedDataPackager<T>>(Supplier<T> empty) implements TrackedDataHandler<T> {
    @Override
    public void write(PacketByteBuf buf, T value) {
        value.toPacket(buf);
    }

    @Override
    public T read(PacketByteBuf buf) {
        return this.empty().get().fromPacket(buf);
    }

    @Override
    public T copy(T value) {
        return value.copyForPackager();
    }
}