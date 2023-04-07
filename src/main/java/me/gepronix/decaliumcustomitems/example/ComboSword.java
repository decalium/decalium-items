/*
 * decalium-items
 * Copyright © 2022 George Pronyuk <https://vk.com/gpronyuk>
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

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierBuilder;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierContainer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

public final class ComboSword implements BuildableItem, Listener {

	private static final AttributeModifierContainer[] ATTRIBUTES = {
			new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_SPEED).slot(EquipmentSlot.HAND).amount(1337).build(),
			new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_DAMAGE).slot(EquipmentSlot.HAND).amount(7).build()
	};

	private final Plugin plugin;
	private final NamespacedKey key;

	public ComboSword(Plugin plugin) {

		this.plugin = plugin;
		this.key = new NamespacedKey(plugin, "combo_axe.yml");
	}
	@Override
	public Item build(ConfigurationNode node) throws SerializationException {
		var builder = new ConfigItem(node).create();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		return builder
				.listener(ItemEventTrigger.KNOCKBACK, (e, ctx) -> {
					e.setCancelled(true);
					e.getEntity().setVelocity(e.getAcceleration());
				})
				.build();
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof LivingEntity entity)) return;
		Optional.of(event.getDamager()).filter(LivingEntity.class::isInstance).map(LivingEntity.class::cast)
				.map(damager -> damager.getEquipment().getItemInMainHand()).filter(i -> !i.getType().isAir())
				.flatMap(DecaliumCustomItems.get().getItemRegistry()::of)
				.filter(item -> item.key().equals(key)).ifPresentOrElse(item -> {
					if(entity.getMaximumNoDamageTicks() != 0) entity.setMetadata("default_damage_ticks", new FixedMetadataValue(plugin, entity.getMaximumNoDamageTicks()));
					entity.setMaximumNoDamageTicks(0);
				}, () -> {
					entity.getMetadata("default_damage_ticks").stream().mapToInt(MetadataValue::asInt)
							.findAny().ifPresent(entity::setMaximumNoDamageTicks);
				});
	}

}
