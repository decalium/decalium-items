package me.gepronix.decaliumcustomitems.example;

import com.destroystokyo.paper.ParticleBuilder;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.ParticleTrail;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.AbstractItem;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collection;
import java.util.function.Predicate;


public class Crystal implements BuildableItem {


    private void onInteract(PlayerInteractEvent event, ItemTriggerContext context) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (player.hasCooldown(context.getItemStack().getType())) return;
        event.setCancelled(true);
        Location playerLocation = player.getLocation();
		Location center = playerLocation.clone();
		ParticleBuilder builder = new ParticleBuilder(Particle.REDSTONE).color(Color.PURPLE).allPlayers();
		int radius = 3;
		for(double t = 0; t <= 2 * Math.PI* radius; t += 0.05) {
			double x = (radius * Math.cos(t)) + center.getX();
			double z = (center.getZ() + radius * Math.sin(t));
			builder.location(center.getWorld(), x, center.getY() + 0.1, z).spawn();
		}
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_HIT, 0.1f, 1.0f);
        Collection<LivingEntity> entities = playerLocation.getNearbyLivingEntities(radius, e -> !e.equals(player) && (!(e instanceof Player p) || p.getGameMode() != GameMode.CREATIVE));
        for(LivingEntity livingEntity : entities) {
			Vector direction = livingEntity.getLocation()
					.subtract(playerLocation)
					.toVector();
			new ParticleTrail(livingEntity.getLocation(), direction, radius + 1).draw();
            livingEntity.damage(11.5, player);
            livingEntity.setVelocity(
                    direction.normalize().multiply(1.15).add(new Vector(0, 0.35, 0)));
        }


        if (player.getGameMode() != GameMode.CREATIVE) context.getItemStack().subtract();
        player.setCooldown(context.getItemStack().getType(), 20);
    }

	@Override
	public Item build(ConfigurationNode node) throws SerializationException {
		return new ConfigItem(node).create().listener(ItemEventTrigger.PLAYER_INTERACT, this::onInteract).build();
	}
}
