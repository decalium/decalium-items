package com.manya.decaliumcustomitems.utils;

import com.manya.decaliumcustomitems.utils.persistentdatatype.BooleanDataType;
import com.manya.decaliumcustomitems.utils.persistentdatatype.LocationTagType;
import com.manya.decaliumcustomitems.utils.persistentdatatype.UUIDDataType;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class DataType {
    public static final BooleanDataType BOOLEAN = new BooleanDataType();
    public static final UUIDDataType UUID = new UUIDDataType();
    public static final LocationTagType LOCATION = new LocationTagType();
public static NamespacedKey key(@NotNull String name) {
    return new NamespacedKey(DecaliumCustomItems.get(), name);
    }
}
