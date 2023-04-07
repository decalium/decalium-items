package me.gepronix.decaliumcustomitems.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class KeyFactory {
    private final Plugin plugin;

    public KeyFactory(Plugin plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey key(String key) {
        return new NamespacedKey(plugin, key);
    }
}
