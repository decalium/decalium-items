package com.manya.decaliumcustomitems.item;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import com.manya.decaliumcustomitems.utils.DataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class CustomMaterial {
    public static final NamespacedKey TYPE = new NamespacedKey(DecaliumCustomItems.get(),"type");
    private static final Map<NamespacedKey, CustomMaterial> materials = new HashMap<>();
    private static final Table<Material, Integer, CustomMaterial> materialsByCustomModelData = HashBasedTable.create();
    private final NamespacedKey key;
    private final Item item;
    private CustomMaterial(Item item, NamespacedKey key) {
        this.key = key;
        this.item = item;
    }
    public Item getItem() {
        return item;
    }

    public static CustomMaterial registerMaterial(NamespacedKey key, Item item) {
        CustomMaterial m = new CustomMaterial(item, key);
        materials.put(key, m);
        return m;
    }
    @Nullable
    public static CustomMaterial of(NamespacedKey key) {
        return materials.get(key);
    }
    @Nullable
    public static CustomMaterial of(@NotNull ItemStack item) {
        if(item.getType() == Material.AIR) return null;
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        return data.has(TYPE, DataType.NAMESPACE_KEY) ? of(data.get(TYPE, DataType.NAMESPACE_KEY)) : getByCustomModelData(item);
    }
    @Nullable
    private static CustomMaterial getByCustomModelData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasCustomModelData()) return null;
        CustomMaterial m = materialsByCustomModelData.get(item.getType(), item.getItemMeta().getCustomModelData());
        if(m != null) {
            m.getItem().itemModifier().modify(item);
            meta.getPersistentDataContainer().set(TYPE, DataType.NAMESPACE_KEY, m.key);
            item.setItemMeta(meta);
        }
        return m;
    }

    public NamespacedKey getKey() { return key;}
    @Override
    public String toString() {
        return key.asString();
    }
}

