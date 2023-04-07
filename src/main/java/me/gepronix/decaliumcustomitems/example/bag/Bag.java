package me.gepronix.decaliumcustomitems.example.bag;

import me.gepronix.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.MetadataHolder;
import me.gepronix.decaliumcustomitems.item.StackOfItems;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag implements Item, Triggerable, MetadataHolder {
    private final NamespacedKey key;
    private final Material original;
    private final ItemModifier modifier;
    private final EventTriggerHolder eventTriggerHolder = new EventTriggerHolderImpl();

    public Bag(Plugin plugin) {
        this.key = new NamespacedKey(plugin, "bag");
        this.original = Material.CHEST;
        this.modifier = ItemModifierImpl.builder()
                .name(Component.text("Мешок").decoration(TextDecoration.ITALIC, false))
                .customModelData(33)
                .lore(this::lore)
                .build();
        eventTriggerHolder.addListener(ItemEventTrigger.PLAYER_INTERACT, this::onInteract);

    }

    public void onInteract(PlayerInteractEvent event, ItemTriggerContext context) {
        ItemStack item = context.getItemStack();

        if (!event.getAction().toString().startsWith("RIGHT_CLICK")) return;
        StackOfItems stack = StackOfItems.of(item);
        BagInventoryHolder holder = new BagInventoryHolder(
                item,
                stack.getCustomMeta()
                        .map(BagMeta.class::cast)
                        .orElse(BagMeta.Factory.INSTANCE.createDefault())
        );
        holder.open(event.getPlayer());
    }

    private List<Component> lore(LivingEntity entity, ItemStack item) {
        List<Component> lore = new ArrayList<>(Collections.singletonList(Component.text("Контент: ")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY)));
        StackOfItems stack = StackOfItems.of(item);
        BagMeta meta = stack.getCustomMeta().map(BagMeta.class::cast).orElse(BagMeta.Factory.INSTANCE.createDefault());
        List<ItemStack> content = meta.content().size() >= 5 ? meta.content().subList(0, 5) : meta.content();
        lore.addAll(content.stream().filter(i -> i.getType() != Material.AIR)
                .map(s -> Component.textOfChildren(Component.translatable(s.getType().getTranslationKey()), Component.text(" x "+s.getAmount()))
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GRAY)).toList());
        return lore;
    }

    @Override
    public ItemMetaFactory<? extends CustomMeta> metaFactory() {
        return BagMeta.Factory.INSTANCE;
    }

    @NotNull
    @Override
    public NamespacedKey key() {
        return key;
    }

    @NotNull
    @Override
    public Material original() {
        return original;
    }

    @NotNull
    @Override
    public ItemModifier modifier() {
        return modifier;
    }

    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return eventTriggerHolder;
    }
}
