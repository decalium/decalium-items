package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class KeyDataType implements PersistentDataType<String, NamespacedKey> {
    private KeyDataType() {}
    public static final KeyDataType INSTANCE = new KeyDataType();
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<NamespacedKey> getComplexType() {
        return NamespacedKey.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull NamespacedKey namespacedKey, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return namespacedKey.asString();
    }

    @Override
    public @NotNull NamespacedKey fromPrimitive(@NotNull String s, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return NamespacedKey.fromString(s);
    }
}
