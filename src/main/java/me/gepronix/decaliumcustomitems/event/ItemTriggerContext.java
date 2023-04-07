package me.gepronix.decaliumcustomitems.event;

import org.bukkit.inventory.ItemStack;

public class ItemTriggerContext {
    private final ItemStack stack;

    public ItemTriggerContext(ItemStack stack) {
        this.stack = stack;

    }

    public ItemStack getItemStack() {
        return stack;
    }
}
