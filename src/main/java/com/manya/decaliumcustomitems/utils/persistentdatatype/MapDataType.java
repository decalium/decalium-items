package com.manya.decaliumcustomitems.utils.persistentdatatype;

import com.google.gson.reflect.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MapDataType<K, V> implements PersistentDataType<PersistentDataContainer[], Map<K, V>> {
    private final NamespacedKey key, value;
    private final PersistentDataType<?, K> keyType;
    private final PersistentDataType<?, V> valueType;
    private final Class<Map<K, V>> complexType;
    public MapDataType(PersistentDataType<?, K> keyType, PersistentDataType<?, V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.key = NamespacedKey.minecraft("key");
        this.value = NamespacedKey.minecraft("value");
        this.complexType = (Class<Map<K, V>>) new TypeToken<Map<K, V>>(){}.getRawType();
    }
    @Override
    public @NotNull Class<PersistentDataContainer[]> getPrimitiveType() {
        return PersistentDataContainer[].class;
    }

    @Override
    public @NotNull Class<Map<K, V>> getComplexType() {
        return complexType;
    }


    @Override
    public @NotNull PersistentDataContainer[] toPrimitive(@NotNull Map<K, V> complex, @NotNull PersistentDataAdapterContext context) {
        return complex.entrySet().stream().map(entry -> {
            PersistentDataContainer pdc = context.newPersistentDataContainer();
            pdc.set(key, keyType, entry.getKey());
            pdc.set(value, valueType, entry.getValue());
            return pdc;
        }).toArray(PersistentDataContainer[]::new);
    }

    @NotNull
    @Override
    public Map<K, V> fromPrimitive(@NotNull PersistentDataContainer[] primitive, @NotNull PersistentDataAdapterContext context) {
        return Arrays.stream(primitive).collect(Collectors.toMap(pdc -> pdc.get(key, keyType), pdc -> pdc.get(value, valueType)));
    }
}
