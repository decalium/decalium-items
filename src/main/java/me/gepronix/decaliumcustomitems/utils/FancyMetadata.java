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
package me.gepronix.decaliumcustomitems.utils;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.OptionalInt;

public final class FancyMetadata {


	private final Plugin plugin;
	private final Metadatable metadatable;

	public FancyMetadata(Plugin plugin, Metadatable metadatable) {
		this.plugin = plugin;
		this.metadatable = metadatable;

	}
	private @Nullable MetadataValue value(String key) {
		for(MetadataValue value : metadatable.getMetadata(key)) {
			if(plugin.equals(value.getOwningPlugin())) return value;
		}
		return null;
	}


	public <T> Optional<T> single(String key, Class<? extends T> type) {
		MetadataValue value = value(key);
		if(!type.isInstance(value)) return Optional.empty();
		return Optional.of(type.cast(value));
	}

	public void set(String key, Object value) {
		metadatable.setMetadata(key, new FixedMetadataValue(plugin, value));
	}
}
