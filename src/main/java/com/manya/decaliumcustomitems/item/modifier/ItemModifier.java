package com.manya.decaliumcustomitems.item.modifier;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemModifier {
    void modify(ItemStack item);
    void modifyVisually(Player player, ItemStack item);
    void unModifyVisually(Player player, ItemStack item);
}
