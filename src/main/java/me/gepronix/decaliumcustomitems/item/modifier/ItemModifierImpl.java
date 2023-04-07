package me.gepronix.decaliumcustomitems.item.modifier;

import com.manya.pdc.DataTypes;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierContainer;
import me.gepronix.decaliumcustomitems.utils.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ItemModifierImpl implements ItemModifier {
    public static final NamespacedKey PROTOCOL_DATA = new NamespacedKey(DecaliumCustomItems.get(), "protocol_data");
    private static final NamespacedKey HAS_DISPLAY_NAME = new NamespacedKey(DecaliumCustomItems.get(), "has_display_name");
    private static final NamespacedKey DYNAMIC_LORE_SIZE = new NamespacedKey(DecaliumCustomItems.get(), "dynamic_lore_size");

    private final DynamicComponent displayName;
	private final DynamicLore lore;
    private final int customModelData;
    private final Consumer<ItemStack> extraModifier;
    private final BiConsumer<LivingEntity, ItemStack> visualModifier, visualUnModifier;
    private final Set<AttributeModifierContainer> attributes;

    private ItemModifierImpl(
            DynamicComponent displayName,
            DynamicLore lore,
            int customModelData,
            Set<AttributeModifierContainer> attributes,
            Consumer<ItemStack> extra,
            BiConsumer<LivingEntity, ItemStack> visualModifier,
            BiConsumer<LivingEntity, ItemStack> visualUnModifier
    ) {
        this.displayName = displayName;
		this.lore = lore;
        this.customModelData = customModelData;
        this.attributes = attributes;
        this.visualModifier = visualModifier;
        this.visualUnModifier = visualUnModifier;
        this.extraModifier = extra;

    }

    @Override
    public void modify(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        attributes.forEach(a ->
                a.apply(meta)
        );
        if (customModelData != 0) meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
		extraModifier.accept(item);
    }

    @Override
    public void modifyVisually(LivingEntity entity, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> result = lore.apply(entity, item);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataContainer protocolData = container.getAdapterContext().newPersistentDataContainer();
        boolean hasDisplayName = false;
        if (!meta.hasDisplayName()) {
            meta.displayName(displayName.apply(entity, item));
        } else {
            hasDisplayName = true;

        }
        protocolData.set(HAS_DISPLAY_NAME, DataTypes.BOOLEAN, hasDisplayName);

        if (!result.isEmpty()) {
            List<Component> newLore = new ArrayList<>(result);
            protocolData.set(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER, result.size());
            if (meta.hasLore()) newLore.addAll(meta.lore());
            meta.lore(newLore);

        }
        container.set(PROTOCOL_DATA, PersistentDataType.TAG_CONTAINER, protocolData);
        item.setItemMeta(meta);
        visualModifier.accept(entity, item);

    }

    @Override
    public void unModifyVisually(LivingEntity entity, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer protocolData = meta.getPersistentDataContainer().get(PROTOCOL_DATA, PersistentDataType.TAG_CONTAINER);
        meta.getPersistentDataContainer().remove(PROTOCOL_DATA);
        if (!protocolData.get(HAS_DISPLAY_NAME, DataTypes.BOOLEAN))
            meta.displayName(null);

        if (meta.lore() != null && protocolData.has(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER)) {
            meta.lore(meta.lore().subList(protocolData.get(DYNAMIC_LORE_SIZE, PersistentDataType.INTEGER), meta.lore().size()));
        }
        item.setItemMeta(meta);
        visualUnModifier.accept(entity, item);

    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DynamicComponent displayName = DynamicComponent.of(null);
        private DynamicLore lore = DynamicLore.of(Collections.emptyList());
        private int customModelData;
        private Set<AttributeModifierContainer> attributeModifiers = new HashSet<>();
        private Consumer<ItemStack> modifier = null;
        private BiConsumer<LivingEntity, ItemStack> visualModifier = (e, i) -> {
        };
        private BiConsumer<LivingEntity, ItemStack> visualUnModifier = (e, i) -> {
        };

        private Builder() {

        }

        public Builder name(Component name) {
            this.displayName = DynamicComponent.of(name);
            return this.modifier(item -> item.editMeta(m -> m.displayName(name)));
        }

        public Builder name(DynamicComponent name) {
            this.displayName = name;
            return this;
        }

        public Builder lore(DynamicLore lore) {
            this.lore = lore;
            return this;
        }

        public Builder lore(List<Component> lore) {
            this.lore = DynamicLore.of(lore);
            return this.modifier(i -> i.editMeta(meta -> meta.lore(lore)));
        }

        public Builder modifier(Consumer<ItemStack> modifier) {
            this.modifier = this.modifier == null ? modifier : this.modifier.andThen(modifier);
            return this;
        }

        public Builder visual(BiConsumer<LivingEntity, ItemStack> visualModifier, BiConsumer<LivingEntity, ItemStack> visualUnModifier) {
            this.visualModifier = visualModifier;
            this.visualUnModifier = visualUnModifier;
            return this;
        }

        public Builder customModelData(int value) {
            customModelData = value;
            return this;
        }

        public Builder attributeModifiers(AttributeModifierContainer... attributes) {
            return attributeModifiers(Arrays.asList(attributes));
        }

		public Builder attributeModifiers(Collection<AttributeModifierContainer> containers) {
			attributeModifiers = new HashSet<>(containers);
			return this;
		}


        public ItemModifierImpl build() {
            return new ItemModifierImpl(
                    displayName,
                    lore,
                    customModelData,
                    attributeModifiers,
                    modifier == null ? i -> {} : modifier,
                    visualModifier,
                    visualUnModifier
            );
        }

    }

}
