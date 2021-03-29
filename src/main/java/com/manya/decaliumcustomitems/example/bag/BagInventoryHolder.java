package com.manya.decaliumcustomitems.example.bag;

import com.manya.decaliumcustomitems.gui.GUI;
import com.manya.decaliumcustomitems.item.WrappedStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BagInventoryHolder implements GUI {
    private final Inventory inventory;
    private final ItemStack bag;
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
    public BagInventoryHolder(ItemStack bag, BagMeta meta) {
        this.bag = bag;
        inventory = Bukkit.createInventory(this, 6 * 9, Component.text("Мешок"));
        inventory.addItem(meta.content().toArray(new ItemStack[meta.content().size()]));

    }
    @Override
    public void onClose(InventoryCloseEvent e) {
        WrappedStack stack = WrappedStack.asCustomStack(bag);
        BagMeta meta = (BagMeta) stack.getCustomMeta();
        meta.setContent(Arrays.asList(inventory.getContents()));
        stack.setCustomMeta(meta);
    }


}
