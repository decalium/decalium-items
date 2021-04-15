package com.manya.decaliumcustomitems.config;

import com.manya.decaliumcustomitems.item.meta.CustomMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import com.manya.decaliumcustomitems.utils.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ConfigurableMeta implements CustomMeta {
    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {return values.get(key); }
    public void set(String key, String value) {
        values.put(key, value);
    }
    private ConfigurableMeta(Map<String, String> values) {

    }
    public static class Factory implements ItemMetaFactory<ConfigurableMeta> {
        public static final Factory INSTANCE = new Factory();
        private Factory() {}

        @Override
        public void save(ConfigurableMeta meta, PersistentDataContainer to) {
            meta.values.forEach((k, v) -> to.set(DataType.key(k), PersistentDataType.STRING, v));
        }

        @Override
        public ConfigurableMeta load(PersistentDataContainer from) {
            Map<String, String> values = new HashMap<>();
            for(NamespacedKey key : from.getKeys()) {
                values.put(key.getKey(), from.get(key, PersistentDataType.STRING));
            }
            return new ConfigurableMeta(values);
        }

        @Override
        public ConfigurableMeta createDefault() {
            return new ConfigurableMeta(new HashMap<>());
        }
    }
}
