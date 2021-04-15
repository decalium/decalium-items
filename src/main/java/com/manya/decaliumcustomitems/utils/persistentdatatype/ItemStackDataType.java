package com.manya.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {
    private ItemStackDataType() {}
    public static final ItemStackDataType INSTANCE = new ItemStackDataType();
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull byte[] toPrimitive(@NotNull ItemStack complex, @NotNull PersistentDataAdapterContext context) {
        return complex.serializeAsBytes();
    }

    @Override
    public @NotNull ItemStack fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return ItemStack.deserializeBytes(primitive);
    }
}
