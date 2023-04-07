package me.gepronix.decaliumcustomitems.example;

import me.gepronix.decaliumcustomitems.ParticleTrail;
import me.gepronix.decaliumcustomitems.Timed;
import me.gepronix.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.MetadataHolder;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class RailGun implements Item, MetadataHolder, Triggerable {

    private final Plugin plugin;
    private final NamespacedKey key;
    private final ItemModifier modifier;
    private final EventTriggerHolder triggerHolder;

    public RailGun(Plugin plugin) {

        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "railgun");
        this.triggerHolder = new EventTriggerHolderImpl();
        this.triggerHolder.addListener(ItemEventTrigger.PLAYER_INTERACT, new Timed<>(this::onInteract, Duration.ofMillis(18 * 100)));
        this.modifier = ItemModifierImpl.builder().name(Component.text("Railgun").decoration(TextDecoration.ITALIC, false))
                .customModelData(2008).build();
    }
    @Override
    public @NotNull EventTriggerHolder eventTriggerHolder() {
        return this.triggerHolder;
    }

    @Override
    public @NotNull NamespacedKey key() {
        return this.key;
    }

    @Override
    public @NotNull Material original() {
        return Material.IRON_AXE;
    }

    @Override
    public @NotNull ItemModifier modifier() {
        return this.modifier;
    }

    @Override
    public ItemMetaFactory<? extends CustomMeta> metaFactory() {
        return CooldownableMeta.Factory.INSTANCE;
    }

    private boolean onInteract(PlayerInteractEvent event, ItemTriggerContext ctx) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return false;
        Player p = event.getPlayer();
        event.setCancelled(true);
        Location head = p.getEyeLocation();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 2);
        RayTraceResult raytrace = p.getWorld().rayTrace(head, head.getDirection(), 300, FluidCollisionMode.NEVER,true,0.3,(pl) -> !pl.equals(p));
        double distance = 300;
        if(raytrace != null) {
            Entity entity = raytrace.getHitEntity();
            Block block = raytrace.getHitBlock();
            if (entity instanceof LivingEntity ent) {
                ent.damage(24f, p);
                distance = head.distance(entity.getLocation());

            } else if (block != null) {
                distance = head.distance(block.getLocation());
            }
        }
        new ParticleTrail(head, head.getDirection().normalize(), distance).draw();
        return true;
    }
}
