package me.gepronix.decaliumcustomitems.utils.persistentdatatype.collection;

import com.google.gson.reflect.TypeToken;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ArrayCollectionDataType<P, C extends Collection<E>, E> extends CollectionDataType<C, P, E> {
    private final Class<P> primitive = (Class<P>) new TypeToken<P>(){}.getRawType();
    private final Supplier<C> collectionFactory;
    private final IntFunction<E[]> arrayGenerator;
    private final PersistentDataType<P, E[]> arrayType;
    public ArrayCollectionDataType(IntFunction<E[]> arrayGenerator, Supplier<C> collectionFactory, PersistentDataType<P, E[]> arrayType) {
        this.arrayType = arrayType;
        this.arrayGenerator = arrayGenerator;
        this.collectionFactory = collectionFactory;
    }

    @Override
    public @NotNull Class<P> getPrimitiveType() {
        return primitive;
    }

    @Override
    public  @NotNull P toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
        return arrayType.toPrimitive(complex.toArray(arrayGenerator.apply(0)), context);
    }

    @Override
    public @NotNull
    C fromPrimitive(@NotNull P primitive, @NotNull PersistentDataAdapterContext context) {
        return Arrays.stream(arrayType.fromPrimitive(primitive, context))
                .collect(Collectors.toCollection(collectionFactory));
    }
}
