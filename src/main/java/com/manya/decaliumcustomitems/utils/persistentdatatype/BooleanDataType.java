package com.manya.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class BooleanDataType implements PersistentDataType<Byte, Boolean> {
    private BooleanDataType() {}
    public static final BooleanDataType INSTANCE = new BooleanDataType();
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Byte toPrimitive(Boolean aBoolean, PersistentDataAdapterContext persistentDataAdapterContext) {
        return aBoolean ? (byte) 1 : 0;
    }

    @Override
    public Boolean fromPrimitive(Byte aByte, PersistentDataAdapterContext persistentDataAdapterContext) {
        return aByte == 1;
    }
}
