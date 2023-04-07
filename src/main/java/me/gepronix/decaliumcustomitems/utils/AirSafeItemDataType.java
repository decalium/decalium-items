package me.gepronix.decaliumcustomitems.utils;

import com.manya.pdc.DataTypes;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AirSafeItemDataType implements PersistentDataType<byte[], ItemStack> {


    public static final AirSafeItemDataType ITEM_STACK = new AirSafeItemDataType();


    private static final byte[] AIR_BYTES = {0xA, 0x1, 0x4};
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemStack itemStack, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        if(itemStack.getType().isAir()) return AIR_BYTES;
        return DataTypes.ITEM_STACK.toPrimitive(itemStack, persistentDataAdapterContext);
    }

    @Override
    public @NotNull ItemStack fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        if(Arrays.equals(AIR_BYTES, bytes)) return new ItemStack(Material.AIR);
        return DataTypes.ITEM_STACK.fromPrimitive(bytes, persistentDataAdapterContext);
    }
}
