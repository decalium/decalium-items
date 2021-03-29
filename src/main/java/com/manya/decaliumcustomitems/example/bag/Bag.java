package com.manya.decaliumcustomitems.example.bag;

import com.manya.decaliumcustomitems.event.EquipmentEventListener;
import com.manya.decaliumcustomitems.item.MetadataHolder;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.item.WrappedStack;
import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.meta.CustomMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bag extends Item implements MetadataHolder {
    public Bag() {
        super(Material.CHEST, DefaultModifier.builder()
                .name(Component.text("Мешок").decoration(TextDecoration.ITALIC, false))
                .customModelData(33)
                .lore(
                        (p, i) -> {
                            List<Component> lore = new ArrayList<>(Arrays.asList(Component.text("Контент: ").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)));
                            WrappedStack stack = WrappedStack.asCustomStack(i);
                            BagMeta meta = (BagMeta) stack.getCustomMeta();
                            lore.addAll(meta.content().stream()
                                    .map(s -> Component.text(s.getType().name() + " x"+s.getAmount())
                                            .decoration(TextDecoration.ITALIC, false)
                                            .color(NamedTextColor.GRAY))
                                    .collect(Collectors.toList()));
                            return lore;
                        }
                )
                .build()
        );
        super.addListener(EquipmentEventListener.PLAYER_INTERACT, this::onInteract, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }
    public void onInteract(ItemStack item, PlayerInteractEvent event) {
        if(!event.getAction().toString().startsWith("RIGHT_CLICK")) return;
        WrappedStack stack = WrappedStack.asCustomStack(item);
        BagInventoryHolder holder = new BagInventoryHolder(item, (BagMeta) stack.getCustomMeta());
        holder.open(event.getPlayer());
    }

    @Override
    public ItemMetaFactory<? extends CustomMeta> metaFactory() {
        return BagMeta.Factory.INSTANCE;
    }
}
