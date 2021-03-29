package com.manya.decaliumcustomitems.listener;

import com.manya.decaliumcustomitems.gui.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null || e.getClickedInventory().getHolder() == null) return;
        InventoryHolder holder = e.getClickedInventory().getHolder();
        if(holder instanceof GUI) {
            ((GUI) holder).onClick(e);
        }
    }
    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if(e.getInventory() == null || e.getInventory().getHolder() == null) return;
        InventoryHolder holder = e.getInventory().getHolder();
        if(holder instanceof GUI) {
            ((GUI) holder).onOpen(e);
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(e.getInventory() == null || e.getInventory().getHolder() == null) return;
        InventoryHolder holder = e.getInventory().getHolder();
        if(holder instanceof GUI) {
            ((GUI) holder).onClose(e);
        }
    }
}
