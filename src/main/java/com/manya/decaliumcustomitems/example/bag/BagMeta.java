package com.manya.decaliumcustomitems.example.bag;

import com.manya.decaliumcustomitems.DecaliumCustomItems;
import com.manya.decaliumcustomitems.item.meta.CustomMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import org.apache.commons.lang.SerializationUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BagMeta implements CustomMeta {
    private List<ItemStack> content;
    private BagMeta(List<ItemStack> content) {
        this.content = content;
    }
    public List<ItemStack> content() {return content; }
    public void setContent(List<ItemStack> content) {
        this.content = content;
    }
    public static class Factory implements ItemMetaFactory<BagMeta> {
        public static final Factory INSTANCE = new Factory();
        private Factory() {}
        private static final NamespacedKey CONTENT = new NamespacedKey(DecaliumCustomItems.get(), "content");
        @Override
        public void save(BagMeta meta, PersistentDataContainer to) {
            ArrayList<byte[]> serialized = new ArrayList<>(meta.content.stream().filter(i -> i != null).map(ItemStack::serializeAsBytes).collect(Collectors.toList()));
            to.set(CONTENT, PersistentDataType.BYTE_ARRAY, SerializationUtils.serialize(serialized));
        }

        @Override
        public BagMeta load(PersistentDataContainer from) {

            try {
                return new BagMeta(
                        ((ArrayList<byte[]>) new ObjectInputStream(new ByteArrayInputStream(from.get(CONTENT, PersistentDataType.BYTE_ARRAY))).readObject())
                                .stream().map(ItemStack::deserializeBytes).collect(Collectors.toList()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public BagMeta createDefault() {
            return new BagMeta(Arrays.asList());
        }
    }

}
