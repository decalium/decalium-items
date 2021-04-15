package com.manya.decaliumcustomitems.utils;

import com.manya.decaliumcustomitems.utils.persistentdatatype.*;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class DataType {
    public static final BooleanDataType BOOLEAN = BooleanDataType.INSTANCE;
    public static final UUIDDataType UUID = UUIDDataType.INSTANCE;
    public static final LocationTagType LOCATION = LocationTagType.INSTANCE;
    public static final ItemStackDataType ITEM_STACK = ItemStackDataType.INSTANCE;
    public static final KeyDataType NAMESPACE_KEY = KeyDataType.INSTANCE;
public static NamespacedKey key(@NotNull String name) {
    return new NamespacedKey(DecaliumCustomItems.get(), name);
    }
}
