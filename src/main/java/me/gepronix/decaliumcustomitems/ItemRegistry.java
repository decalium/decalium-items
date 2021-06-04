package me.gepronix.decaliumcustomitems;

import me.gepronix.decaliumcustomitems.item.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * this interface represents a registry of items.
 *
 */
public interface ItemRegistry {
    /**
     * register an item
     * @param item - item to register
     */
    void registerItem(@NotNull Item item);

    /**
     *
     * @return collection of registered items
     */
    @NotNull Collection<Item> items();

    /**
     * returns keys of registered items
     * @return collection of keys
     */
    @NotNull Collection<NamespacedKey> keys();

    /**
     * removes item from registry
     * @param item - item to remove
     */
    default void removeItem(@NotNull Item item) {
        removeItem(item.key());
    }

    /**
     * removes all registered items
     */
    void clear();

    /**
     * removes item from registry by key
     * @param key - key of the item
     */
    void removeItem(@NotNull NamespacedKey key);

    /**
     * returns an
     * @param stack - custom itemstack
     * @return Optional of itemStack's custom material.
     */
    Optional<Item> of(@NotNull ItemStack stack);
    Optional<Item> of(@NotNull NamespacedKey key);

    /**
     *
     * @return a key for custom item type in pdc.
     */
    @NotNull NamespacedKey getTypeKey();




}

