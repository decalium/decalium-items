package me.gepronix.decaliumcustomitems.item.modifier;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * An ItemStack modifier. Used for adding attributes on custom item stacks.
 */
public interface ItemModifier {
    /**
     * Modifies itemStack attributes. Called on custom item stack initialization
     *
     * @param item - item stack to modify
     */
    void modify(ItemStack item);

    /**
     * Modifies item stack visually, only on client side.
     * Used in packet listeners.
     *
     * @param entity - Entity that holds item
     * @param item   - item stack to modify
     */
    void modifyVisually(@Nullable LivingEntity entity, ItemStack item);

    /**
     * Removes visual attributes of item.
     * Used in packet listeners.
     *
     * @param entity - Entity that holds item
     * @param item   - item stack to modify
     */
    void unModifyVisually(@Nullable LivingEntity entity, ItemStack item);
}
