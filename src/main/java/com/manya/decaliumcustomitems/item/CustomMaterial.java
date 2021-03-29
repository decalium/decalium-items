package com.manya.decaliumcustomitems.item;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.persistence.PersistentDataType.STRING;

public class CustomMaterial {
    public static final NamespacedKey TYPE = new NamespacedKey(DecaliumCustomItems.get(),"type");
    private static final Map<String, CustomMaterial> materials = new HashMap<>();
    private static final Table<Material, Integer, CustomMaterial> materialsByCustomModelData = HashBasedTable.create();
    private final String id;
    private final Item item;
    private CustomMaterial(Item item, String id) {
        this.id = id;
        this.item = item;
    }
    public Item getItem() {
        return item;
    }

    public static void registerMaterial(String id, Item item) {
        materials.put(id, new CustomMaterial(item, id));
    }
    @Nullable
    public static CustomMaterial of(String id) {
        return materials.get(id);
    }
    @Nullable
    public static CustomMaterial of(@NotNull ItemStack item) {
        if(item.getType() == Material.AIR) return null;
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        return data.has(TYPE, STRING) ? of(data.get(TYPE, STRING)) : getByCustomModelData(item);
    }
    @Nullable
    private static CustomMaterial getByCustomModelData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasCustomModelData()) return null;
        CustomMaterial m = materialsByCustomModelData.get(item.getType(), item.getItemMeta().getCustomModelData());
        if(m != null) {
            m.getItem().itemModifier().modify(item);
            meta.getPersistentDataContainer().set(TYPE, STRING, m.id);
            item.setItemMeta(meta);
        }
        return m;
    }

    public String getId() { return id;}
    @Override
    public String toString() {
        return id;
    }
}

