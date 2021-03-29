package com.manya.decaliumcustomitems.item.modifier;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DynamicComponent extends BiFunction<Player, ItemStack, Component> {
    static DynamicComponent of(Component c) {
        return (p, i) -> c;
    }
}
