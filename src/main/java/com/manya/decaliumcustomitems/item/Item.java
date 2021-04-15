package com.manya.decaliumcustomitems.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.manya.decaliumcustomitems.event.EquipmentEventListener;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import com.manya.decaliumcustomitems.item.modifier.ItemModifier;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;

public class Item {
    private final Table<
            EquipmentEventListener<? extends Event>,
            EquipmentSlot,
            BiConsumer<ItemStack, ? extends Event>
            > executors = HashBasedTable.create();


    private final ItemModifier modifier;
    private final Material original;
    @Nullable
    private ItemMetaFactory<?> metadataFactory;

    public Item(@NotNull  Material original, @NotNull ItemModifier modifier) {
        this.modifier = modifier;
        this.original = original;
    }
    private Item(Material material,
                 ItemModifier modifier,
                 Table<EquipmentEventListener<? extends Event>, EquipmentSlot,
                         BiConsumer<ItemStack, ? extends Event>> eventListeners,
                 @Nullable ItemMetaFactory<?> metadataFactory) {
        this.metadataFactory = metadataFactory;
        this.original = material;
        this.modifier = modifier;
        executors.putAll(eventListeners);

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
    public static Builder builder(Material original) {
        return new Builder(original);
    }

    public ItemModifier itemModifier() {
        return modifier;
    }
    @Nullable
    public ItemMetaFactory<?> metadataFactory() {return metadataFactory; }
    public static class Builder {
        private final Material original;
        private ItemModifier modifier;
        private ItemMetaFactory<?> metaFactory;
        private Table<EquipmentEventListener<? extends Event>, EquipmentSlot, BiConsumer<ItemStack, ? extends Event>> executors = HashBasedTable.create();
        private Builder(Material original) {
            this.original = original;
        }
        public Builder modifier(ItemModifier modifier) {
            this.modifier = modifier;
            return this;
        }
        public <T extends Event> Builder listener(EquipmentEventListener<T> type, BiConsumer<ItemStack, T> executor, EquipmentSlot... slots) {
            for(EquipmentSlot slot : slots) {
                executors.put(type, slot, executor);
            }
            return this;
        }
        public Builder metadataFactory(ItemMetaFactory<?> metaFactory) {
            this.metaFactory = metaFactory;
            return this;
        }
        public Item build() {
            return new Item(Objects.requireNonNull(original), modifier, executors, metaFactory);
        }
    }





}