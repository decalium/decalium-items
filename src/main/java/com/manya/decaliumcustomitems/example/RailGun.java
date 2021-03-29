package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import com.manya.decaliumcustomitems.item.WrappedStack;
import com.manya.decaliumcustomitems.item.Interactable;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class RailGun extends Item implements Interactable {
    private int damage = 40;
    private double cooldown = 2;
    public RailGun() {
        super(Material.IRON_AXE, DefaultModifier.builder().name(Component.text("railgun")).build());
    }
    @Override
    public void onInteract(PlayerInteractEvent e) {
        final ItemStack item = e.getItem();
        WrappedStack stack = WrappedStack.asCustomStack(item);
        ItemMeta meta = item.getItemMeta();
            new BukkitRunnable() {
                public void run() {
                    shoot(e.getPlayer());
                }
            }.runTaskLater(DecaliumCustomItems.get(),3);


        }

    private void shoot(Player p) {

        Location head = p.getEyeLocation();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 2);
        RayTraceResult raytrace = p.getWorld().rayTrace(head, head.getDirection(), 300, FluidCollisionMode.NEVER,true,0.6,(pl) -> !pl.equals(p));
        double distance = 300;
        if(raytrace != null) {
            Entity entity = raytrace.getHitEntity();
            Block block = raytrace.getHitBlock();
            if (entity != null && entity instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) entity;
                ent.damage(damage, p);
                distance = head.distance(entity.getLocation());

            } else if (block != null) {
                distance = head.distance(block.getLocation());
            }
        }
        drawParticleTrail(head,head.getDirection(),(int) distance);



    }
    private void drawParticleTrail(Location start, Vector direction, int distance) {
        Vector increase = direction;
        Location point = start;
        for (int counter = 0; counter < distance; counter++) {
            point.add(increase);
            start.getWorld().spawnParticle(Particle.REDSTONE, point.getX(), point.getY(), point.getZ(), 2,0, 0, 0, 0, new Particle.DustOptions(Color.BLUE, 1));

        }
    }
}
