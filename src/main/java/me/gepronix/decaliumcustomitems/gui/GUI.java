package me.gepronix.decaliumcustomitems.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GUI extends InventoryHolder {
    default void open(Player player) {player.openInventory(getInventory()); }
    default void onClick(InventoryClickEvent e) {}
    default void onClose(InventoryCloseEvent e) {}
    default void onOpen(InventoryOpenEvent e) {}

}
