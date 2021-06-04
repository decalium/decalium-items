package me.gepronix.decaliumcustomitems;

import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.AbstractItem;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;


public class Crystal extends AbstractItem {

    public Crystal(Plugin plugin) {
        super(new NamespacedKey(plugin, "crystal"), Material.BLAZE_ROD);
        listener(ItemEventTrigger.PLAYER_INTERACT, this::onInteract);
        setModifier(
                ItemModifierImpl.builder()
                        .name(Component.text("Кристалл").decoration(TextDecoration.ITALIC, false))
                        .customModelData(33)
                        .build()
        );

    }
    private void onInteract(PlayerInteractEvent event, ItemTriggerContext context) {
        if(!event.getAction().name().startsWith("LEFT_CLICK")) return;
        Player player = event.getPlayer();
        if(player.hasCooldown(original())) return;
        event.setCancelled(true);
        Location playerLocation = player.getLocation();
        playerLocation.getNearbyLivingEntities(6).stream()
                .filter(entity -> entity != player)
                .forEach(livingEntity -> {
                    livingEntity.damage(40, player);
                    livingEntity.setVelocity(
                            livingEntity.getLocation()
                                    .subtract(playerLocation)
                                    .toVector()
                                    .normalize().multiply(1.15).add(new Vector(0, 0.35, 0))
                    );
                });


        if(player.getGameMode() != GameMode.CREATIVE) context.getItemStack().subtract();
        player.setCooldown(original(), 20);
    }
}
