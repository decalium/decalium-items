package me.gepronix.decaliumcustomitems.item.meta;

import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CooldownableMeta implements CustomMeta {
    private long lastUsage;
    public long lastUsage() {return lastUsage; }
    public void setLastUsage(long value) {
        lastUsage = value;
    }

    public CooldownableMeta(long lastUsage) {
        this.lastUsage = lastUsage;
    }

    @Override
    public ItemMetaFactory<?> getFactory() {
        return Factory.INSTANCE;
    }

    public static class Factory implements ItemMetaFactory<CooldownableMeta> {
        public static final Factory INSTANCE = new Factory();
        private Factory() {}
        private static final NamespacedKey LAST_USAGE = new NamespacedKey(DecaliumCustomItems.get(), "last_usage");
        @Override
        public void save(CooldownableMeta meta, PersistentDataContainer to) {
            to.set(LAST_USAGE, PersistentDataType.LONG, meta.lastUsage);
        }

        @Override
        public CooldownableMeta load(PersistentDataContainer from) {
            return new CooldownableMeta(from.get(LAST_USAGE, PersistentDataType.LONG));
        }

        @Override
        public CooldownableMeta createDefault() {
            return new CooldownableMeta(0);
        }
    }

}
