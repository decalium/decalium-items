package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import com.google.gson.reflect.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MapDataType<C extends Map<K, V> ,K, V> implements PersistentDataType<PersistentDataContainer[], C> {
    private final NamespacedKey key, value;
    private final PersistentDataType<?, K> keyType;
    private final PersistentDataType<?, V> valueType;
    private final Class<C> complexType;
    private final Supplier<C> mapFactory;
    @SuppressWarnings("unchecked")
    public MapDataType(PersistentDataType<?, K> keyType, PersistentDataType<?, V> valueType, Supplier<C> mapFactory) {
        this.mapFactory = mapFactory;
        this.keyType = keyType;
        this.valueType = valueType;
        this.key = NamespacedKey.minecraft("key");
        this.value = NamespacedKey.minecraft("value");

        this.complexType = (Class<C>) new TypeToken<C>(){}.getRawType();
    }
    @Override
    public
    @NotNull Class<PersistentDataContainer[]> getPrimitiveType() {
        return PersistentDataContainer[].class;
    }

    @Override
    public
    @NotNull Class<C> getComplexType() {
        return complexType;
    }


    @Override
    public @NotNull PersistentDataContainer[] toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
        return complex.entrySet().stream().map(entry -> {
            PersistentDataContainer pdc = context.newPersistentDataContainer();
            pdc.set(key, keyType, entry.getKey());
            pdc.set(value, valueType, entry.getValue());
            return pdc;
        }).toArray(PersistentDataContainer[]::new);
    }

    @Override
    public @NotNull C fromPrimitive(@NotNull PersistentDataContainer[] primitive, @NotNull PersistentDataAdapterContext context) {
        return Arrays.stream(primitive)
                .collect(Collectors.toMap(
                        pdc -> pdc.get(key, keyType),
                        pdc -> pdc.get(value, valueType),
                        (existing, replacement) -> existing,
                        mapFactory
                ));
    }
}
