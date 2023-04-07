package me.gepronix.decaliumcustomitems.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class AttributeModifierBuilder {

    private final Attribute attribute;
    private double amount;
    private String name;
    private UUID uuid = UUID.randomUUID();
    private AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
    private EquipmentSlot slot = EquipmentSlot.HAND;

    public AttributeModifierBuilder(Attribute attribute) {
        this.attribute = attribute;
        name(attribute.getKey().getKey());
    }

    public AttributeModifierBuilder amount(double amount) {
        this.amount = amount;
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

    public double amount() {
        return amount;
    }

    public Attribute attribute() {
        return attribute;
    }

    public String name() {
        return name;
    }

    public UUID uuid() {
        return uuid;
    }

    public AttributeModifier.Operation operation() {
        return operation;
    }

    public EquipmentSlot slot() {
        return slot;
    }


    public AttributeModifierContainer build() {
        return new AttributeModifierContainer(
                attribute,
                new AttributeModifier(
                        uuid,
                        name,
                        amount,
                        operation,
                        slot
                )
        );
    }


}

