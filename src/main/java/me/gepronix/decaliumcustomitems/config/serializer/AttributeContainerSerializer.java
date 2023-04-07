/*
 * decalium-items
 * Copyright © 2023 George Pronyuk <https://vk.com/gpronyuk>
 *
 * decalium-items is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * decalium-items is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with decalium-items. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package me.gepronix.decaliumcustomitems.config.serializer;

import me.gepronix.decaliumcustomitems.utils.AttributeModifierBuilder;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierContainer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public final class AttributeContainerSerializer implements TypeSerializer<AttributeModifierContainer> {


	@Override
	public AttributeModifierContainer deserialize(Type type, ConfigurationNode node) throws SerializationException {
		Attribute attribute = node.node("type").require(Attribute.class);
		String name = node.node("name").getString(attribute.name().toLowerCase(Locale.ROOT));
		EquipmentSlot slot = node.node("slot").get(EquipmentSlot.class, EquipmentSlot.HAND);
		AttributeModifier.Operation operation = node.node("operation").get(AttributeModifier.Operation.class, AttributeModifier.Operation.ADD_NUMBER);
		double amount = node.node("amount").getDouble();
		return new AttributeModifierBuilder(attribute).name(name).slot(slot).operation(operation).amount(amount).build();
	}

	@Override
	public void serialize(Type type, @Nullable AttributeModifierContainer obj, ConfigurationNode node) throws SerializationException {
		if(obj == null) return;
		node.node("type").set(obj.attribute());
		String attrName = obj.attribute().name().toLowerCase(Locale.ROOT);
		if(!attrName.equals(obj.modifier().getName())) node.node("name").set(obj.modifier().getName());
		if(obj.modifier().getSlot() != EquipmentSlot.HAND) node.node("slot").set(obj.modifier().getSlot());
		if(obj.modifier().getOperation() != AttributeModifier.Operation.ADD_NUMBER) node.node("operation").set(obj.modifier().getOperation());
		node.node("amount").set(obj.modifier().getAmount());
	}
}
