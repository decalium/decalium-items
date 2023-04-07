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

import com.destroystokyo.paper.util.SneakyThrow;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.ItemRegistry;
import me.gepronix.decaliumcustomitems.example.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierContainer;
import me.gepronix.decaliumcustomitems.utils.JarUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public final class ConfigItems {
	private static final ConfigurationOptions OPTIONS = ConfigurationOptions.defaults()
			.serializers(builder -> builder
					.register(AttributeModifierContainer.class, new AttributeContainerSerializer())
					.register(Component.class, new ComponentSerializer())
					.register(Material.class, new MaterialSerializer())
					.register(NamespacedKey.class, new NamespacedKeySerializer())
			);
	private final File dataFolder;
	private final Logger logger;
	private final Plugin plugin;

	public ConfigItems(Plugin plugin) {
		this.plugin = plugin;
		this.dataFolder = plugin.getDataFolder();
		this.logger = plugin.getSLF4JLogger();
	}


	public Item loadBuiltin(String name, BuildableItem item) throws SerializationException, ParsingException {
		File builtins = new File(dataFolder,  "builtins");
		var node = YamlConfigurationLoader.builder().file(new File(builtins, name+".yml")).build().load(OPTIONS);
		return item.build(node);
	}

	public Item loadCustom(File file) throws SerializationException, ParsingException {
		var node = YamlConfigurationLoader.builder().file(file).build().load(OPTIONS);
		return new ConfigItem(node).create().build();
	}

	public void tryLoading(ItemRegistry registry, String name, BuildableItem item) {
		try {
			Item i = loadBuiltin(name, item);
			registry.registerItem(i);
		} catch (SerializationException | ParsingException | NoSuchElementException e) {
			logger.error("Failed to load item " + name, e);
		}
	}


	public void loadBuiltins(ItemRegistry registry) {
		try {
			JarUtil.copyFolderFromJar("builtins", dataFolder, JarUtil.CopyOption.COPY_IF_NOT_EXIST);
		} catch (IOException e) {
			logger.error("Failed to copy builtins!", e);
		}

		tryLoading(registry, "big_sword", new BigSword());
		tryLoading(registry, "cannon", new Cannon());
		tryLoading(registry, "combo_axe", new ComboSword(plugin));
		tryLoading(registry, "katana_sword", new Katana());
		tryLoading(registry, "crystal", new Crystal());
		tryLoading(registry, "gauss", new Gauss());
	}

	public void loadCustom(ItemRegistry registry) {
		File custom = new File(dataFolder, "custom");
		custom.mkdir();
		for(File file : custom.listFiles()) {
			try {
				registry.registerItem(loadCustom(file));
			} catch (SerializationException | ParsingException | NoSuchElementException e) {
				logger.error("Failed to load item " + file, e);
			}
		}
	}



}
