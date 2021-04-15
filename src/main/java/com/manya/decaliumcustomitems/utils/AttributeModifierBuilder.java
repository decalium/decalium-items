package com.manya.decaliumcustomitems.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class AttributeModifierBuilder {

    private final Attribute attribute;
    private double value;
    private String name;
    private UUID uuid = UUID.randomUUID();
    private AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
    private EquipmentSlot slot = EquipmentSlot.HAND;
    public AttributeModifierBuilder(Attribute attribute) {
        this.attribute = attribute;
        name(attribute.getKey().getKey());
    }
    public AttributeModifierBuilder value(double value) {
        this.value = value;
        return this;
    }
    public AttributeModifierBuilder name(String name) {
        this.name = name;
        return this;
    }
    public AttributeModifierBuilder uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
    public AttributeModifierBuilder operation(AttributeModifier.Operation operation) {
        this.operation = operation;
        return this;
    }
    public AttributeModifierBuilder slot(EquipmentSlot slot) {
        this.slot = slot;
        return this;
    }
    public AttributeModifierContainer build() {
        return new AttributeModifierContainer(
                attribute,
                new AttributeModifier(
                        uuid,
                        name,
                        value,
                        operation,
                        slot
                )
        );
    }


}

