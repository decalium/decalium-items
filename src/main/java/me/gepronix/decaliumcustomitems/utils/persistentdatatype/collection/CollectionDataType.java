package me.gepronix.decaliumcustomitems.utils.persistentdatatype.collection;

import com.google.gson.reflect.TypeToken;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Supplier;

public abstract class CollectionDataType<C extends Collection<E>, P, E> implements PersistentDataType<P, C> {

    private final Class<C> complex = (Class<C>) new TypeToken<C>(){}.getRawType();
    @Override
    public @NotNull Class<C> getComplexType() {
        return complex;
    }
    @SuppressWarnings("unchecked")
    public static <C extends Collection<E>, E> CollectionDataType<C, ?, E> of(PersistentDataType<?, E> type, Supplier<C> collectionFactory) {
        Class<?> primitive = type.getPrimitiveType();
        if(primitive.equals(byte[].class)) {
            return new ByteArrayCollectionDataType<>((PersistentDataType<byte[], E>) type, collectionFactory);
        } else if(primitive.isArray()) {

        }
        return null;
    }
}
