package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.item.Interactable;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Gauss extends Item implements Interactable {
    public Gauss() {
        super(Material.GOLDEN_HOE, DefaultModifier.builder().name(Component.text("Gauss")).build());
    }
    @Override
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            Location headloc = p.getEyeLocation();
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,1);
            p.setVelocity(headloc.getDirection().multiply(-2));
        }
    }
}
