package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

public class UUIDDataType implements ByteArrayDataType<UUID> {
    private UUIDDataType() {}
    public static final UUIDDataType INSTANCE = new UUIDDataType();
    private static final int UUID_SIZE = 16;
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(UUID uuid, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer buf = ByteBuffer.allocate(UUID_SIZE);
        buf.putLong(uuid.getLeastSignificantBits());
        buf.putLong(uuid.getMostSignificantBits());
        return buf.array();
    }

    @NotNull
    @Override
    public UUID fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        long least = buf.getLong();
        long most = buf.getLong();
        return new UUID(least, most);
    }

    @Override
    public boolean hasFixedLength() {
        return true;
    }

    @Override
    public Optional<Integer> getFixedLength() {
        return Optional.of(UUID_SIZE);
    }
}
