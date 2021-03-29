package com.manya.decaliumcustomitems.item;

import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class InteractListener implements Listener {
    private final DecaliumCustomItems plugin;

    public InteractListener(DecaliumCustomItems plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        if(e.getItem() == null || e.getItem().getType() == Material.AIR) return;
        CustomMaterial material = CustomMaterial.of(e.getItem());
        if(material != null && material.getItem() instanceof Interactable) {
            ((Interactable) material.getItem()).onInteract(e); 
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

    }
}
