package me.gepronix.decaliumcustomitems.event;

import com.google.common.collect.ImmutableMap;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.ItemRegistry;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ItemEventTrigger<T extends Event> implements EventTrigger<T, ItemTriggerContext> {
    public static final ItemRegistry ITEM_REGISTRY = DecaliumCustomItems.get().getItemRegistry();
    public static final ItemEventTrigger<PlayerInteractEvent> PLAYER_INTERACT = new ItemEventTrigger<>(
            PlayerInteractEvent.class,
            item(PlayerInteractEvent::getItem)
    );
    public static final ItemEventTrigger<BlockPlaceEvent> BLOCK_PLACE = new ItemEventTrigger<>(
            BlockPlaceEvent.class,
            item(BlockPlaceEvent::getItemInHand)
    );


    private final Class<T> eventClass;
    private final Function<T, Collection<ItemStack>> itemGetter;
    public ItemEventTrigger(
            @NotNull Class<T> eventClass,
            @NotNull Function<T, Collection<ItemStack>> itemGetter
    ) {
        this.eventClass = eventClass;
        this.itemGetter = itemGetter;
    }
    @Override
    public Map<Triggerable, ItemTriggerContext> handle(T event) {
        final ImmutableMap.Builder<Triggerable, ItemTriggerContext> mapBuilder = ImmutableMap.builder();
        for(ItemStack itemStack : itemGetter.apply(event)) {
            if(itemStack == null) continue;
            ITEM_REGISTRY.of(itemStack)
                    .filter(Triggerable.class::isInstance)
                    .map(Triggerable.class::cast)
                    .ifPresent(item -> mapBuilder.put(item, new ItemTriggerContext(itemStack)));
        }
        return mapBuilder.build();

    }

    @Override
    public Class<T> getEventClass() {
        return eventClass;
    }
    public static <T extends Event> Function<T, Collection<ItemStack>> equipment(Function<T, EntityEquipment> equipmentGetter, EquipmentSlot... slots) {
        return t -> {
            EntityEquipment eq = equipmentGetter.apply(t);
            return Arrays.stream(slots).map(eq::getItem).collect(Collectors.toList());
        };

    }
    public static <T extends Event> Function<T, Collection<ItemStack>> equipment(Function<T, EntityEquipment> equipmentGetter) {
        return equipment(equipmentGetter, EquipmentSlot.values());
    }

    public static <T extends Event> Function<T, Collection<ItemStack>> item(Function<T, ItemStack> itemGetter) {
        return t -> Collections.singletonList(itemGetter.apply(t));
    }

}
