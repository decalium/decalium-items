package com.manya.decaliumcustomitems.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.manya.decaliumcustomitems.protocol.wrapper.WrapperPlayServerSetSlot;
import com.manya.decaliumcustomitems.item.CustomMaterial;
import com.manya.decaliumcustomitems.protocol.wrapper.WrapperPlayClientSetCreativeSlot;
import com.manya.decaliumcustomitems.protocol.wrapper.WrapperPlayServerWindowItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ItemListener extends PacketAdapter {
    public ItemListener(Plugin plugin) {
        super(plugin, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Server.SET_SLOT, PacketType.Play.Client.SET_CREATIVE_SLOT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if(packet.getType() == PacketType.Play.Server.SET_SLOT) {
            WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(packet);
            CustomMaterial material = CustomMaterial.of(setSlot.getSlotData());
            if(material != null) {
                ItemStack toModify = setSlot.getSlotData();
                material.getItem().itemModifier().modifyVisually(event.getPlayer(), toModify);
                setSlot.setSlotData(toModify);
            }
        } else if (packet.getType() == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems windowItems = new WrapperPlayServerWindowItems(packet);
            List<ItemStack> result = new ArrayList<>();
            for(ItemStack item : windowItems.getSlotData()) {
                if(item == null || item.getType() == Material.AIR) {
                    result.add(item);
                    continue;
                }
                CustomMaterial material = CustomMaterial.of(item);
                if(material != null)
                    material.getItem().itemModifier().modifyVisually(event.getPlayer(), item);
                result.add(item);

            }
            windowItems.setSlotData(result);
        }
        event.setPacket(packet);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if(event.getPacket().getType() == PacketType.Play.Client.SET_CREATIVE_SLOT) {
            WrapperPlayClientSetCreativeSlot creativeSlot = new WrapperPlayClientSetCreativeSlot(packet);
            ItemStack item = creativeSlot.getClickedItem();
            CustomMaterial material = CustomMaterial.of(item);
            if(material != null) {
                material.getItem().itemModifier().unModifyVisually(event.getPlayer(), item);
                creativeSlot.setClickedItem(item);
            }
            event.setPacket(packet);
        }
    }
}
