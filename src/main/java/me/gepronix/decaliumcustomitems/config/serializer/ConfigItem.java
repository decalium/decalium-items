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
package me.gepronix.decaliumcustomitems.config.serializer;

import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;

public final class ConfigItem {

	private final ConfigurationNode node;

	public ConfigItem(ConfigurationNode node) {
		this.node = node;
	}


	public SimpleItem.Builder create() throws SerializationException {
		Material material = node.node("material").require(Material.class);
		NamespacedKey key = node.node("key").require(NamespacedKey.class);

		Component displayName = Component.text().append(node.node("display-name").require(Component.class)).decoration(TextDecoration.ITALIC, false).build();
		List<Component> lore = node.node("lore").getList(Component.class, List.of()).stream().<Component>map(c -> Component.text().append(c).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false).build()).toList();
		int customModelData = node.node("custom-model-data").getInt();
		List<AttributeModifierContainer> modifiers = node.node("attributes").getList(AttributeModifierContainer.class, List.of());
		return SimpleItem.builder(material).key(key)
				.modifier(
						ItemModifierImpl.builder()
								.name(displayName)
								.lore(lore)
								.customModelData(customModelData)
								.attributeModifiers(modifiers)
								.build()
				);
	}

}
