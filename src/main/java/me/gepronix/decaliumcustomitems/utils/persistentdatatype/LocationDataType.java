package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import me.gepronix.decaliumcustomitems.utils.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;


public class LocationDataType implements PersistentDataType<byte[], Location> {
    private LocationDataType() {}
    public static final LocationDataType INSTANCE = new LocationDataType();
    private static final int UUID_BYTE_SIZE = 16;
    private static final int DOUBLE_BYTE_SIZE = 8;
    private static final int FLOAT_BYTE_SIZE = 4;
    private static final int LOCATION_SIZE  = UUID_BYTE_SIZE + DOUBLE_BYTE_SIZE * 3 + FLOAT_BYTE_SIZE * 2;

    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull Location location, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer buf = ByteBuffer.allocate(LOCATION_SIZE);
        buf.put(DataType.UUID.toPrimitive(location.getWorld().getUID(), context));
        buf.putDouble(location.getX());
        buf.putDouble(location.getY());
        buf.putDouble(location.getZ());
        buf.putFloat(location.getYaw());
        buf.putFloat(location.getPitch());
        return buf.array();
    }

    @NotNull
    @Override
    public Location fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer buf = ByteBuffer.wrap(primitive);
        byte[] uuid = new byte[UUID_BYTE_SIZE];
        buf.get(uuid);
        World world = Bukkit.getWorld(DataType.UUID.fromPrimitive(uuid, context));
        double x = buf.getDouble();
        double y = buf.getDouble();
        double z = buf.getDouble();
        float yaw = buf.getFloat();
        float pitch = buf.getFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }
}
