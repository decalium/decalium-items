package com.manya.decaliumcustomitems.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.manya.decaliumcustomitems.event.EquipmentEventListener;
import com.manya.decaliumcustomitems.item.modifier.ItemModifier;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class Item {
    private final Table<EquipmentEventListener<? extends Event>, EquipmentSlot, BiConsumer<ItemStack, ? extends Event>> executors = HashBasedTable.create();


    private final ItemModifier modifier;
    private final Material original;

    public Item(Material original, ItemModifier modifier) {
        this.modifier = modifier;
        this.original = original;
    }
    public Material getOriginal() {
        return original;
    }
    public <T extends Event> BiConsumer<ItemStack, T> executor(EquipmentEventListener<T> listener, EquipmentSlot slot) {
        return (BiConsumer<ItemStack, T>) executors.get(listener, slot);

    }
    public <T extends Event> void addListener(EquipmentEventListener<T> listener, BiConsumer<ItemStack, T> consumer, EquipmentSlot... slots) {
       for(EquipmentSlot slot : slots) {
           executors.put(listener, slot, consumer);
       }
    }

    public ItemModifier itemModifier() {
        return modifier;
    }





}
