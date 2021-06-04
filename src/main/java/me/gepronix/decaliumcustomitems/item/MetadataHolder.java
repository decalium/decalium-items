package me.gepronix.decaliumcustomitems.item;

import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;

/**
 * An entry that can hold metadata.
 */
public interface MetadataHolder {
    /**
     *
     * @return Meta factory
     */
    ItemMetaFactory<? extends CustomMeta> metaFactory();
}
