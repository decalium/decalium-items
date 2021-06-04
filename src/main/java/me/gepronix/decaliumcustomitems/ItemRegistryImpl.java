package me.gepronix.decaliumcustomitems;

import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.utils.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemRegistryImpl implements ItemRegistry {
    private final NamespacedKey typeKey;
    private final Map<NamespacedKey, Item> registeredItems = new HashMap<>();
    public ItemRegistryImpl(@NotNull Plugin plugin) {
        this.typeKey = new NamespacedKey(plugin, "type");
    }




    @Override
    public void registerItem(@NotNull Item item) {
        if(registeredItems.containsKey(item.key())) throw new IllegalStateException("item already registered");
        registeredItems.put(item.key(), item);
    }

    @Override
    public @NotNull Collection<Item> items() {
        return Collections.unmodifiableCollection(registeredItems.values());
    }

    @Override
    public @NotNull Collection<NamespacedKey> keys() {
        return Collections.unmodifiableCollection(registeredItems.keySet());
    }

    @Override
    public void clear() {
        registeredItems.clear();
    }

    @Override
    public void removeItem(@NotNull NamespacedKey key) {
        if(!registeredItems.containsKey(key)) throw new NoSuchElementException("no item with given key found.");
        registeredItems.remove(key);
    }

    @Override
    public Optional<Item> of(@NotNull ItemStack stack) {
        PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = container.get(typeKey, DataType.NAMESPACE_KEY);
        return key == null ? Optional.empty() : of(key);
    }

    @Override
    public Optional<Item> of(@NotNull NamespacedKey key) {
        return Optional.ofNullable(registeredItems.get(key));
    }

    @NotNull
    @Override
    public NamespacedKey getTypeKey() {
        return typeKey;
    }
}
