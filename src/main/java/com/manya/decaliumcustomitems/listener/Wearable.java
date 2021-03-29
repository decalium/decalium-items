package com.manya.decaliumcustomitems.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.manya.decaliumcustomitems.item.CustomMaterial;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface Wearable {
    void onEquip(PlayerArmorChangeEvent e);
    void onUnEquip(PlayerArmorChangeEvent e);

    class EquipListener implements Listener {
        @EventHandler
        public void onEquip(PlayerArmorChangeEvent e) {
            ItemStack newArmor = e.getNewItem();
            ItemStack oldArmor = e.getOldItem();
            if(newArmor != null) {
                CustomMaterial material = CustomMaterial.of(newArmor);
                if(material != null && material.getItem() instanceof Wearable)
                    ((Wearable) material.getItem()).onEquip(e);
            }
            if(oldArmor != null) {
                CustomMaterial material = CustomMaterial.of(oldArmor);
                if(material != null && material.getItem() instanceof Wearable)
                    ((Wearable) material.getItem()).onUnEquip(e);
            }
        }
    }
    static void registerListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new EquipListener(), plugin);
    }
}
