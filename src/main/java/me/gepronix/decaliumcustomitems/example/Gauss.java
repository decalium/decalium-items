/*
 * decalium-items
 * Copyright © 2023 George Pronyuk <https://vk.com/gpronyuk>
 *
 * decalium-items is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * decalium-items is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with decalium-items. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package me.gepronix.decaliumcustomitems.example;

import com.destroystokyo.paper.ParticleBuilder;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.ParticleTrail;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.item.Item;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public final class Gauss implements BuildableItem {
	@Override
	public Item build(ConfigurationNode node) throws SerializationException {
		return new ConfigItem(node).create().listener(ItemEventTrigger.PLAYER_INTERACT, (event, ctx) -> {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				event.setCancelled(true);
				Player p = event.getPlayer();
				Location headloc = p.getEyeLocation();
				Vector velocity = headloc.getDirection().multiply(-2.4).add(p.getVelocity());
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_ADD_ITEM,1,1);
				Optional.ofNullable(event.getInteractionPoint()).ifPresent(location -> {
					new ParticleTrail(location,
							velocity,
							velocity.length() * 10,
							new ParticleBuilder(Particle.REDSTONE).color(Color.YELLOW).allPlayers()
					).draw();
				});
				p.setVelocity(velocity);
			}
		}).build();
	}
}
