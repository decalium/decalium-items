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
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.Item;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class BigSword implements BuildableItem {
	@Override
	public Item build(ConfigurationNode node) throws SerializationException {
		return new ConfigItem(node).create().listener(ItemEventTrigger.DAMAGE, this::onDamage).build();
	}

	private void onDamage(EntityDamageByEntityEvent event, ItemTriggerContext ctx) {
		if(!(event.getDamager() instanceof Player entity)) return;
		if(entity.getAttackCooldown() < 1) return;
		double damage = event.getDamage();
		var entities = event.getEntity().getLocation().getNearbyLivingEntities(1.2, e -> !e.equals(event.getEntity()));
		new ParticleBuilder(Particle.SWEEP_ATTACK).allPlayers().location(event.getEntity().getLocation()).spawn();
		entities.forEach(e -> e.damage(damage / entities.size() + 0.5));
	}
}
