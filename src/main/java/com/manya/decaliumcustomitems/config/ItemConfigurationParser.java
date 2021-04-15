package com.manya.decaliumcustomitems.config;

import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.utils.AttributeModifierBuilder;
import com.manya.decaliumcustomitems.utils.AttributeModifierContainer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemConfigurationParser {
    private final ModifierParser modifierParser = new ModifierParser();

    public void parse(FileConfiguration configuration) {
        Item.Builder builder = Item.builder(Material.valueOf(configuration.getString("original-material")));
        builder.modifier(modifierParser.parse(configuration.getConfigurationSection("modifier")));
        builder.metadataFactory(ConfigurableMeta.Factory.INSTANCE);


    }
    static class ModifierParser {
        private final MiniMessage mm = MiniMessage.get();
        private ModifierParser() {

        }
        public DefaultModifier parse(ConfigurationSection section) {

            DefaultModifier.Builder builder = DefaultModifier.builder();
            final String name = section.getString("name");
            builder.name(
                    ((player, itemStack) -> mm.parse(PlaceholderAPI.setPlaceholders(player, name)))
            );
            final List<String> lore = section.getStringList("lore");
            builder.lore(
                    ((player, itemStack) -> lore.stream().map(
                            s -> mm.parse(PlaceholderAPI.setPlaceholders(player, s))).collect(Collectors.toList())
                    )
            );
            ConfigurationSection attributes = section.getConfigurationSection("attribute-modifiers");
            List<AttributeModifierContainer> attributeModifiers = new ArrayList<>();
            for(String key : attributes.getKeys(false)) {
                ConfigurationSection attribute = attributes.getConfigurationSection(key);
                attributeModifiers.add(
                        new AttributeModifierBuilder(Attribute.valueOf(attribute.getString("type")))
                        .name(key)
                        .operation(AttributeModifier.Operation.valueOf(attribute.getString("operation", "ADD_NUMBER")))
                        .slot(EquipmentSlot.valueOf(attribute.getString("slot")))
                        .value(attribute.getDouble("value"))
                        .build()
                        );
            }

            builder.attributeModifiers(attributeModifiers.toArray(new AttributeModifierContainer[0]));
            builder.customModelData(section.getInt("custom-model-data"));
            return builder.build();


        }
    }
}
