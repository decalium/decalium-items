package me.gepronix.decaliumcustomitems.item;

import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * this interface represents custom Material.
 * It contains Key, Original Material and Item Modifier
 */
public interface Item {
    /**
     * Returns a key of Item
     *
     * @return item key
     */
    @NotNull
    NamespacedKey key();

    /**
     * original material that custom material based on.
     *
     * @return original material
     */
    @NotNull
    Material original();

    /**
     * @return Item modifier of this material.
     */
    @NotNull
    ItemModifier modifier();

}
