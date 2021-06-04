package me.gepronix.decaliumcustomitems.example.bag;

import me.gepronix.decaliumcustomitems.gui.GUI;
import me.gepronix.decaliumcustomitems.item.WrappedStack;
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
        inventory.setContents(meta.content().toArray(new ItemStack[0]));

    }
    @Override
    public void onClose(InventoryCloseEvent e) {
        WrappedStack stack = WrappedStack.of(bag);
        BagMeta meta = stack.getCustomMeta().map(BagMeta.class::cast)
                .orElseGet(BagMeta.Factory.INSTANCE::createDefault);
        meta.setContent(Arrays.asList(inventory.getContents()));
        stack.setCustomMeta(meta);
    }


}
