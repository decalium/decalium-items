package me.gepronix.decaliumcustomitems.protocol.wrapper;

import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import java.util.List;

public class WrapperPlayServerEntityEquipment extends AbstractPacket {
    public static final PacketType TYPE =
            PacketType.Play.Server.ENTITY_EQUIPMENT;

    public WrapperPlayServerEntityEquipment() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityEquipment(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Entity ID.
     * <p>
     * Notes: entity's ID
     *
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Entity ID.
     *
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    public ItemSlot getSlot() {
        return handle.getItemSlots().read(0);
    }

    public void setSlot(ItemSlot value) {
        handle.getItemSlots().write(0, value);
    }

    /**
     * Retrieve Item.
     * <p>
     * Notes: item in slot format
     *
     * @return The current Item
     */
    public ItemStack getItem() {
        return handle.getItemModifier().read(0);
    }

    /**
     * Set Item.
     *
     * @param value - new value.
     */
    public void setItem(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }
    public List<Pair<ItemSlot, ItemStack>> getSlotStackPairs() {
        return handle.getSlotStackPairLists().read(0);
    }

    /**
     * Set a ItemSlot - ItemStack pair.
     * @param slot The slot the item will be equipped in. If matches an existing pair, will overwrite the old one
     * @param item The item to equip
     */
    public void setSlotStackPairs(List<Pair<ItemSlot, ItemStack>> pairs) {

        handle.getSlotStackPairLists().write(0, pairs);
    }
}
