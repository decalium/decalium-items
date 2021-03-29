package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.item.Interactable;
import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Shaft extends Item implements Interactable {
    private double damage;
    public Shaft() {
        super(Material.IRON_HOE, DefaultModifier.builder().build());
        this.damage = 4;
    }
    @Override
    public void onInteract(PlayerInteractEvent e) {
                shoot(e.getPlayer());
    }
    private void shoot(Player p) {
        Location head = p.getEyeLocation();
        RayTraceResult raytrace = p.getWorld().rayTrace(head,head.getDirection(),60, FluidCollisionMode.NEVER,true,0.6,(pl) -> !pl.equals(p));


        p.getWorld().playSound(head, Sound.ENTITY_SPLASH_POTION_THROW,1,5);
        double distance = 100;
        if(raytrace != null) {
            Entity entity = raytrace.getHitEntity();
            Block block = raytrace.getHitBlock();
            if (entity != null && entity instanceof LivingEntity) {
                final LivingEntity ent = (LivingEntity) entity;
                final int mndt = ent.getMaximumNoDamageTicks();
                ent.setMaximumNoDamageTicks(1);
                ent.damage(damage, p);
                Bukkit.getScheduler().runTaskLater(DecaliumCustomItems.get(), new Runnable() {
                    public void run() {
                        ent.setMaximumNoDamageTicks(mndt);
                    }
                }, 2);
                distance = head.distance(entity.getLocation());

            } else if (block != null) {
                distance = head.distance(block.getLocation());
            }
        }
        drawParticleTrail(head,head.getDirection(),(int)distance);




    }
    private void drawParticleTrail(Location start, Vector direction, int distance) {
        Vector increase = direction;
        Location point = start;
        for (int counter = 0; counter < distance; counter++) {
            point.add(increase);
            start.getWorld().spawnParticle(Particle.REDSTONE, point.getX(), point.getY(), point.getZ(), 2,0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1));

        }
    }
}
