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
package me.gepronix.decaliumcustomitems;

import me.gepronix.decaliumcustomitems.item.Item;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public interface BuildableItem {
	Item build(ConfigurationNode node) throws SerializationException;

	interface NoConfig extends BuildableItem {

		@Override
		default Item build(ConfigurationNode node) throws SerializationException {
			return build();
		}

		Item build();
	}

}
