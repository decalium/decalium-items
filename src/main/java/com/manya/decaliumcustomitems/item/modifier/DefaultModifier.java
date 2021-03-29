package com.manya.decaliumcustomitems.item.modifier;

import com.manya.decaliumcustomitems.DecaliumCustomItems;
import com.manya.decaliumcustomitems.utils.AttributeModifierContainer;
import com.manya.decaliumcustomitems.utils.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DefaultModifier implements ItemModifier {
    public static final NamespacedKey PROTOCOL_DATA = new NamespacedKey(DecaliumCustomItems.get(), "protocol_data");
    private static final NamespacedKey HAS_DISPLAY_NAME = new NamespacedKey(DecaliumCustomItems.get(), "has_display_name");
    private static final NamespacedKey DYNAMIC_LORE_SIZE = new NamespacedKey(DecaliumCustomItems.get(), "dynamic_lore_size");

    private final BiFunction<Player, ItemStack, Component> displayName;
    private final DynamicLore lore;
    private final int customModelData;
    private final Consumer<ItemStack> extraModifier;
    private final VisualModifier extraVisualModifier;
    private final Set<AttributeModifierContainer> attributes;
    private DefaultModifier(
            BiFunction<Player, ItemStack,Component> displayName,
            DynamicLore lore,
            int customModelData,
            Set<AttributeModifierContainer> attributes,
            Consumer<ItemStack> extra,
            VisualModifier visual
    ) {
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
        this.attributes = attributes;
        this.extraVisualModifier = visual;
        this.extraModifier = extra;

    }

    @Override
    public void modify(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        attributes.forEach(a ->
            a.apply(meta)
        );
        if(customModelData != 0) meta.setCustomModelData(customModelData);
        extraModifier.accept(item);
        item.setItemMeta(meta);
    }

    @Override
    public void modifyVisually(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> result = lore.apply(player, item);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataContainer protocolData = container.getAdapterContext().newPersistentDataContainer();
        boolean hasDisplayName = false;
        if(!meta.hasDisplayName()) {
            meta.displayName(displayName.apply(player, item));
        } else {
            hasDisplayName = true;

        }
        protocolData.set(HAS_DISPLAY_NAME, DataType.BOOLEAN, hasDisplayName);

        if(!result.isEmpty()) {
            List<Component> newLore = new ArrayList<>(result);
            protocolData.set(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER, result.size());
            if (meta.hasLore()) newLore.addAll(meta.lore());
            meta.lore(newLore);

        }
        container.set(PROTOCOL_DATA, PersistentDataType.TAG_CONTAINER, protocolData);
        item.setItemMeta(meta);
        extraVisualModifier.modify(player, item);

    }

    @Override
    public void unModifyVisually(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer protocolData = meta.getPersistentDataContainer().get(PROTOCOL_DATA, PersistentDataType.TAG_CONTAINER);
        meta.getPersistentDataContainer().remove(PROTOCOL_DATA);
        if(!protocolData.get(HAS_DISPLAY_NAME, DataType.BOOLEAN))
            meta.displayName(null);

        if(protocolData.has(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER)) {
            meta.lore(meta.lore().subList(protocolData.get(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER), meta.lore().size()));
        }
        item.setItemMeta(meta);
        extraVisualModifier.unModify(player, item);

    }
    public interface VisualModifier {
        void modify(Player player, ItemStack item);
        void unModify(Player player, ItemStack item);

        static VisualModifier of(Consumer<ItemStack> modifier, Consumer<ItemStack> unModifier) {
            return new VisualModifier() {
                @Override
                public void modify(Player player, ItemStack item) {
                    modifier.accept(item);
                }

                @Override
                public void unModify(Player player, ItemStack item) {
                    unModifier.accept(item);
                }
            };
        }

    }


    public static Builder builder() {return new Builder(); }
    public static class Builder {
        private BiFunction<Player, ItemStack, Component> displayName;
        private DynamicLore lore;
        private int customModelData;
        private Set<AttributeModifierContainer> attributeModifiers = new HashSet<>();
        private Consumer<ItemStack> extra = i -> {};
        private VisualModifier extraVisual = VisualModifier.of(i -> {}, i -> {});
        private Builder() {

        }
        public Builder name(Component name) {
            this.displayName = (p, i) -> name;
            return this;
        }
        public Builder name(BiFunction<Player, ItemStack, Component> name) {
            this.displayName = name;
            return this;
        }

        public Builder lore(DynamicLore lore) {
            this.lore = lore;
            return this;
        }
        public Builder lore(List<Component> lore) {
            this.lore = DynamicLore.of(lore);
            return this;
        }
        public Builder extraModifier(Consumer<ItemStack> modifier) {
            extra = modifier;
            return this;
        }
        public Builder visual(VisualModifier visual) {
           extraVisual = visual;
            return this;
        }
        public Builder customModelData(int value) {
            customModelData = value;
            return this;
        }

        public Builder attributeModifiers(AttributeModifierContainer... attributes) {
            attributeModifiers = new HashSet<>(Arrays.asList(attributes));
            return this;
        }



        public DefaultModifier build() {
            return new DefaultModifier(
                    displayName,
                    lore,
                    customModelData,
                    attributeModifiers,
                    extra,
                    extraVisual
            );
        }

    }

}
