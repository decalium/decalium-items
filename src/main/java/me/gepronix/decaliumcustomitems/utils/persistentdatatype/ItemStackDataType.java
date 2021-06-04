package me.gepronix.decaliumcustomitems.utils.persistentdatatype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {
    private ItemStackDataType() {}
    public static final ItemStackDataType INSTANCE = new ItemStackDataType();
    private static final ItemStack AIR_ITEM = new ItemStack(Material.AIR);
    private static final byte[] AIR = new byte[] {27, 115};
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
        return complex.getType() == Material.AIR ? AIR : complex.serializeAsBytes();
    }

    @Override
    public @NotNull ItemStack fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        return Arrays.equals(AIR, primitive) ? AIR_ITEM : ItemStack.deserializeBytes(primitive);
    }
}
