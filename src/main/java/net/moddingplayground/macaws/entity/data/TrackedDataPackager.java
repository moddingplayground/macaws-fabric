package net.moddingplayground.macaws.entity.data;

import net.minecraft.network.PacketByteBuf;

public interface TrackedDataPackager<T> {
    void toPacket(PacketByteBuf buf);
    T fromPacket(PacketByteBuf buf);
    T copyForPackager();
}
