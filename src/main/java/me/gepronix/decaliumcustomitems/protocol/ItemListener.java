package me.gepronix.decaliumcustomitems.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.Pair;
import me.gepronix.decaliumcustomitems.ItemRegistry;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.protocol.wrapper.WrapperPlayClientSetCreativeSlot;
import me.gepronix.decaliumcustomitems.protocol.wrapper.WrapperPlayServerEntityEquipment;
import me.gepronix.decaliumcustomitems.protocol.wrapper.WrapperPlayServerSetSlot;
import me.gepronix.decaliumcustomitems.protocol.wrapper.WrapperPlayServerWindowItems;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

public class ItemListener extends PacketAdapter {
    private final ItemRegistry registry;

    public ItemListener(Plugin plugin, ItemRegistry registry) {
        super(plugin,
                PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Client.SET_CREATIVE_SLOT,
                PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.registry = registry;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getType() == PacketType.Play.Server.SET_SLOT) {
            var setSlot = new WrapperPlayServerSetSlot(packet);
            ItemStack stack = setSlot.getSlotData();
            processItemModify(event.getPlayer(), stack);
            setSlot.setSlotData(stack);

        } else if (packet.getType() == PacketType.Play.Server.WINDOW_ITEMS) {
            var windowItems = new WrapperPlayServerWindowItems(packet);
            List<ItemStack> itemStacks = windowItems.getSlotData();
            for (ItemStack itemStack : itemStacks) {
                processItemModify(event.getPlayer(), itemStack);
            }

            windowItems.setSlotData(itemStacks);
        } else if (packet.getType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            var equipment = new WrapperPlayServerEntityEquipment(packet);
            List<Pair<ItemSlot, ItemStack>> slotStackPairs = equipment.getSlotStackPairs();
            for (Pair<ItemSlot, ItemStack> pair : slotStackPairs) {
                Entity entity = equipment.getEntity(event);
                processItemModify(entity instanceof LivingEntity ? (LivingEntity) entity : null, pair.getSecond());
            }
            equipment.setSlotStackPairs(slotStackPairs);
        }
        event.setPacket(packet);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        WrapperPlayClientSetCreativeSlot creativeSlot = new WrapperPlayClientSetCreativeSlot(event.getPacket());
        ItemStack stack = creativeSlot.getClickedItem();
        if (stack == null || stack.getType() == Material.AIR) return;
        registry.of(stack).map(Item::modifier)
                .ifPresent(modifier -> modifier.unModifyVisually(event.getPlayer(), stack));
        creativeSlot.setClickedItem(stack);
        event.setPacket(creativeSlot.getHandle());

    }

    private void processItemModify(@Nullable LivingEntity entity, @Nullable final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;
        registry.of(item).map(Item::modifier)
                .ifPresent(modifier -> modifier.modifyVisually(entity, item));
    }

}
 