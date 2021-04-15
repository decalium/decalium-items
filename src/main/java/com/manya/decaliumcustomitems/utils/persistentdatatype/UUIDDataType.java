package com.manya.decaliumcustomitems.utils.persistentdatatype;

import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UUIDDataType implements PersistentDataType<PersistentDataContainer, UUID> {
    private UUIDDataType() {}
    public static final UUIDDataType INSTANCE = new UUIDDataType();
    private static final NamespacedKey MOST = new NamespacedKey(DecaliumCustomItems.get(),"uuid_most");
    private static final NamespacedKey LEAST = new NamespacedKey(DecaliumCustomItems.get(),"uuid_least");
    @NotNull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @NotNull
    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @NotNull
    @Override
    public PersistentDataContainer toPrimitive(UUID uuid, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(MOST, PersistentDataType.LONG, uuid.getMostSignificantBits());
        container.set(LEAST, PersistentDataType.LONG, uuid.getLeastSignificantBits());
        return container;
    }

    @NotNull
    @Override
    public UUID fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext persistentDataAdapterContext) {
        return new UUID(
                container.get(MOST, PersistentDataType.LONG),
                container.get(LEAST, PersistentDataType.LONG)
        );
    }
}
