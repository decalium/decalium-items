package com.manya.decaliumcustomitems.utils.persistentdatatype;

import com.google.gson.reflect.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListDataType<T> implements PersistentDataType<PersistentDataContainer[], List<T>> {
    private static final NamespacedKey VALUE = NamespacedKey.minecraft("value");
    private final PersistentDataType<?, T> type;
    private final Class<List<T>> complex;
    public ListDataType(PersistentDataType<?, T> type) {
        complex = (Class<List<T>>) new TypeToken<List<T>>(){}.getRawType();
        this.type = type;
    }
    public static <T> ListDataType<T> of(PersistentDataType<?, T> type) {return new ListDataType<>(type);}
    @Override
    public @NotNull Class<PersistentDataContainer[]> getPrimitiveType() {
        return PersistentDataContainer[].class;
    }

    @Override
    public @NotNull Class<List<T>> getComplexType() {
        return complex;
    }

    @Override
    public @NotNull PersistentDataContainer[] toPrimitive(@NotNull List<T> complex, @NotNull PersistentDataAdapterContext context) {

       return complex.stream().map(t -> {
            PersistentDataContainer c = context.newPersistentDataContainer();
            c.set(VALUE, type, t);
            return c;
        }).toArray(PersistentDataContainer[]::new);
    }

    @Override
    public @NotNull List<T> fromPrimitive(@NotNull PersistentDataContainer[] primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return Arrays.stream(primitive).map(pdc -> pdc.get(VALUE, type)).collect(Collectors.toList());
    }
}
