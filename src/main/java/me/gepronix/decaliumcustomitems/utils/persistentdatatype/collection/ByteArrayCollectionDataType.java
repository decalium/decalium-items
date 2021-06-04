package me.gepronix.decaliumcustomitems.utils.persistentdatatype.collection;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ByteArrayCollectionDataType<C extends Collection<E>, E> extends CollectionDataType<C, byte[], E> {
    private static final int INTEGER_BYTE_SIZE = 4;
    private final PersistentDataType<byte[], E> type;
    private final ByteFlatterer flatterer;
    private final Supplier<C> collectionFactory;

    public ByteArrayCollectionDataType(PersistentDataType<byte[], E> type, Supplier<C> collectionFactory) {
       this(new ByteFlatterer.DynamicLengthByteFlatterer(), type, collectionFactory);
    }
    public ByteArrayCollectionDataType(int fixedElementLength, PersistentDataType<byte[], E> type, Supplier<C> collectionFactory) {
        this(new ByteFlatterer.FixedLengthByteFlatterer(fixedElementLength), type, collectionFactory);
    }
    private ByteArrayCollectionDataType(ByteFlatterer flatterer,
                                        PersistentDataType<byte[], E> type,
                                        Supplier<C> collectionFactory) {
        this.flatterer = flatterer;
        this.type = type;
        this.collectionFactory = collectionFactory;

    }
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull byte[] toPrimitive(@NotNull C es, @NotNull PersistentDataAdapterContext context) {
        return flatterer.serialize(es.stream().map(e -> type.toPrimitive(e, context)).toArray(byte[][]::new));
    }

    @Override
    public @NotNull C fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext context) {
      return Arrays.stream(flatterer.deserialize(bytes))
              .map(b -> type.fromPrimitive(b, context))
              .collect(Collectors.toCollection(collectionFactory));

    }
    private interface ByteFlatterer {
        byte[] serialize(byte[][] bytes);
        byte[][] deserialize(byte[] bytes);

        class DynamicLengthByteFlatterer implements ByteFlatterer{

            @Override
            public byte[] serialize(byte[][] bytes) {
                int totalSize = Arrays.stream(bytes).mapToInt(b -> b.length).sum();
                ByteBuffer buf = ByteBuffer.allocate(totalSize + bytes.length * INTEGER_BYTE_SIZE);
                for(byte[] b : bytes) {
                    buf.putInt(b.length);
                    buf.put(b);
                }
                return buf.array();
            }

            @Override
            public byte[][] deserialize(byte[] bytes) {
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                ArrayList<byte[]> bytesList = new ArrayList<>();
                while(buf.remaining() > 0) {
                    if(buf.remaining() < INTEGER_BYTE_SIZE) break;
                    int size = buf.getInt();
                    if(buf.remaining() < size) break;
                    byte[] b = new byte[size];
                    buf.get(b);
                    bytesList.add(b);
                }
                return bytesList.toArray(new byte[0][]);
            }
        }
        class FixedLengthByteFlatterer implements ByteFlatterer {
            private final int fixedLength;
            public FixedLengthByteFlatterer(final int fixedLength) {
                this.fixedLength = fixedLength;
            }

            @Override
            public byte[] serialize(byte[][] bytes) {
                ByteBuffer buf = ByteBuffer.allocate(bytes.length * fixedLength);
                for(byte[] b : bytes) {
                    buf.put(b);
                }
                return buf.array();
            }

            @Override
            public byte[][] deserialize(byte[] bytes) {
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                ArrayList<byte[]> byteList = new ArrayList<>(bytes.length / fixedLength);
                while(buf.remaining() > 0) {
                    if(buf.remaining() < fixedLength) break;
                    byte[] b = new byte[fixedLength];
                    buf.get(b);
                    byteList.add(b);
                }
                return byteList.toArray(new byte[0][]);
            }
        }
    }
}
