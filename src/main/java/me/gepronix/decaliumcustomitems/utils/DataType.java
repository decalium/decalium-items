package me.gepronix.decaliumcustomitems.utils;

import com.manya.decaliumcustomitems.utils.persistentdatatype.*;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.utils.persistentdatatype.*;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class DataType {
    private static final KeyFactory DecaliumKeyFactory = new KeyFactory(DecaliumCustomItems.get());

    public static final BooleanDataType BOOLEAN = BooleanDataType.INSTANCE;
    public static final UUIDDataType UUID = UUIDDataType.INSTANCE;
    public static final LocationDataType LOCATION = LocationDataType.INSTANCE;
    public static final ItemStackDataType ITEM_STACK = ItemStackDataType.INSTANCE;
    public static final KeyDataType NAMESPACE_KEY = KeyDataType.INSTANCE;

    public static NamespacedKey key(@NotNull String name) {
    return DecaliumKeyFactory.key(name);
    }
}
