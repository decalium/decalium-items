package com.manya.decaliumcustomitems.utils.persistentdatatype;

import com.manya.decaliumcustomitems.utils.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class LocationTagType implements PersistentDataType<PersistentDataContainer, Location> {
    private static final NamespacedKey WORLD = DataType.key("location_world");
    private static final NamespacedKey X = DataType.key("location_x");
    private static final NamespacedKey Y = DataType.key("location_y");
    private static final NamespacedKey Z = DataType.key("location_z");
    private static final NamespacedKey YAW = DataType.key("location_yaw");
    private static final NamespacedKey PITCH = DataType.key("location_pitch");
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(Location location, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(WORLD, DataType.UUID,location.getWorld().getUID());
        container.set(X,DOUBLE,location.getX());
        container.set(Y,DOUBLE,location.getY());
        container.set(Z,DOUBLE,location.getZ());
        container.set(YAW,FLOAT,location.getYaw());
        container.set(PITCH,FLOAT,location.getPitch());
        return container;
    }

    @Override
    public Location fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext persistentDataAdapterContext) {

        return new Location(Bukkit.getWorld(container.get(WORLD, DataType.UUID)),
                    container.get(X,DOUBLE),
                    container.get(Y,DOUBLE),
                    container.get(Z,DOUBLE),
                    container.get(YAW,FLOAT),
                    container.get(PITCH,FLOAT));
    }
}
