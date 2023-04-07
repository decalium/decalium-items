package me.gepronix.decaliumcustomitems.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeModifierContainer {
    private final Attribute attribute;
    private final AttributeModifier modifier;

    public AttributeModifierContainer(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Attribute attribute() {
        return attribute;
    }

    public AttributeModifier modifier() {
        return modifier;
    }

    public void apply(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        apply(meta);
        stack.setItemMeta(meta);
    }

    public void apply(ItemMeta meta) {
        meta.addAttributeModifier(attribute, modifier);
    }
}
