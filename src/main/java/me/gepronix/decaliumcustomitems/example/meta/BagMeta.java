package me.gepronix.decaliumcustomitems.example.meta;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BagMeta {
    private final ItemStack[] items;
    private final UUID owner;

    public ItemStack[] items() {
        return items;
    }
    public UUID owner() {
        return owner;
    }
    public BagMeta(UUID uuid, ItemStack[] items) {
        this.owner = uuid;
        this.items = items;
    }


}
