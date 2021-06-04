package me.gepronix.decaliumcustomitems.item.modifier;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DynamicComponent extends BiFunction<LivingEntity, ItemStack, Component> {
    static DynamicComponent of(Component c) {
        return (p, i) -> c;
    }
}
