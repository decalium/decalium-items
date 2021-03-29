package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.item.Interactable;
import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlasmaGun extends Item implements Interactable {
    public PlasmaGun() {
        super(Material.DIAMOND_AXE, DefaultModifier.builder().name(Component.text("plasma")).build());
    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Location headloc = p.getEyeLocation();
        Entity entity = p.getWorld().spawnEntity(headloc.add(headloc.getDirection()), EntityType.SMALL_FIREBALL);
        entity.setVelocity(headloc.getDirection());
        p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 30, 1);
    }
}