package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public interface ByteArrayDataType<C> extends PersistentDataType<byte[], C> {
    boolean hasFixedLength();
    Optional<Integer> getFixedLength();
}
