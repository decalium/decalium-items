package me.gepronix.decaliumcustomitems.utils.persistentdatatype.collection;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class StringArrayDataType implements PersistentDataType<byte[], String[]> {
    private static final int INTEGER_BYTE_SIZE = 4;
    private final Charset charset;
    public StringArrayDataType(Charset charset) {
        this.charset = charset;
    }
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<String[]> getComplexType() {
        return String[].class;
    }

    @Override
    public @NotNull byte[] toPrimitive(@NotNull String[] complex, @NotNull PersistentDataAdapterContext context) {
        byte[][] bytes = Arrays.stream(complex).map(s -> s.getBytes(charset)).toArray(byte[][]::new);
        int totalSize = Arrays.stream(bytes).mapToInt(b -> b.length).sum();
        ByteBuffer buf = ByteBuffer.allocate(
                totalSize + // total size
                        (INTEGER_BYTE_SIZE * bytes.length) //
        );
        for(byte[] b : bytes) {
            buf.putInt(b.length);
            buf.put(b);
        }
        return buf.array();
    }

    @Override
    public @NotNull String[] fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer buf = ByteBuffer.wrap(primitive);
        ArrayList<String> stringList = new ArrayList<>();
        while(buf.remaining() > 0) {
            if(buf.remaining() < 4) break;
            int size = buf.getInt();
            if(buf.remaining() < size) break;
            byte[] b = new byte[size];
            buf.get(b);
            stringList.add(new String(b, charset).intern());
        }
        return stringList.toArray(new String[0]);
    }
}
