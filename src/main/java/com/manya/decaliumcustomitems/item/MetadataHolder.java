package com.manya.decaliumcustomitems.item;

import com.manya.decaliumcustomitems.item.meta.CustomMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;

public interface MetadataHolder {
    ItemMetaFactory<? extends CustomMeta> metaFactory();
}
