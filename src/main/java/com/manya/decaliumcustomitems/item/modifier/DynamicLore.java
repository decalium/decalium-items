package com.manya.decaliumcustomitems.item.modifier;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public interface DynamicLore extends BiFunction<Player, ItemStack, List<Component>> {
    static DynamicLore of(List<Component> lore) {
        return (p, i) -> lore;
    }
}
