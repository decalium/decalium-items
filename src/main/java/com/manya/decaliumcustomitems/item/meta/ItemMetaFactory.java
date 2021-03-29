package com.manya.decaliumcustomitems.item.meta;

import org.bukkit.persistence.PersistentDataContainer;

public interface ItemMetaFactory<T extends CustomMeta> {
    void save(T meta, PersistentDataContainer to);
    T load(PersistentDataContainer from);
    T createDefault();
}
