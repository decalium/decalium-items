package com.manya.decaliumcustomitems.listener;

import com.manya.decaliumcustomitems.item.CustomMaterial;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface Selectable {
    void onSelect(PlayerItemHeldEvent event);
    void onUnSelect(PlayerItemHeldEvent event);

    class SelectListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onHeld(PlayerItemHeldEvent e) {
            Inventory inv = e.getPlayer().getInventory();
            ItemStack previous = inv.getItem(e.getPreviousSlot());
            ItemStack next = inv.getItem(e.getNewSlot());
            CustomMaterial previousMaterial = previous == null ? null : CustomMaterial.of(previous);
            CustomMaterial nextMaterial = next == null ? null : CustomMaterial.of(next);
            if(nextMaterial != null && nextMaterial.getItem() instanceof Selectable)
                ((Selectable) nextMaterial.getItem()).onSelect(e);
            if(previousMaterial != null && previousMaterial.getItem() instanceof Selectable)
                ((Selectable) previousMaterial.getItem()).onUnSelect(e);
        }
    }
    static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new SelectListener(), plugin);
    }
}

