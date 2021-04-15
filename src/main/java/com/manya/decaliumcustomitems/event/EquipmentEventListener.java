package com.manya.decaliumcustomitems.event;

import com.manya.decaliumcustomitems.DecaliumCustomItems;
import com.manya.decaliumcustomitems.item.CustomMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EquipmentEventListener<T extends Event> implements EventListener<T> {
    private static Map<String, EquipmentEventListener<?>> eventListeners = new HashMap<>();
    public static final EquipmentEventListener<PlayerInteractEvent> PLAYER_INTERACT = registerListener(
            "PLAYER_INTERACT",
            PlayerInteractEvent.class,
            PlayerInteractEvent::getPlayer
    );

    private final Function<T, LivingEntity> entityGetter;
    private final Class<T> eventClass;

    public static <T extends Event> EquipmentEventListener<T> registerListener(
            Plugin plugin,
            String name,
            Class<T> eventClass,
            Function<T, LivingEntity> entityGetter
    ) {
        EquipmentEventListener<T> listener = new EquipmentEventListener<>(entityGetter, eventClass);
        Bukkit.getPluginManager().registerEvent(eventClass, listener, EventPriority.HIGH, (l, e) -> listener.handle((T) e), plugin);
         return listener;
    }
    public static <T extends Event> EquipmentEventListener<T> registerListener(String name, Class<T> eventClass, Function<T, LivingEntity> entityGetter) {
        return registerListener(DecaliumCustomItems.get(), name, eventClass, entityGetter);
    }

    private EquipmentEventListener(Function<T, LivingEntity> entityGetter, Class<T> eventClass) {
        this.entityGetter = entityGetter;
        this.eventClass = eventClass;

    }
    @Nullable
    public static EquipmentEventListener<?> getByName(String name) {
        return eventListeners.get(name);
    }



    @EventHandler
    public void handle(T event) {
        LivingEntity entity = entityGetter.apply(event);
        if(entity == null || !entity.isValid()) return;
        EntityEquipment equipment = entity.getEquipment();
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = equipment.getItem(slot);
            if(item == null || item.getType().isAir()) continue;
            CustomMaterial material = CustomMaterial.of(item);
            if(material == null) continue;
            BiConsumer<ItemStack, T> executor = material.getItem().executor(this, slot);
            if(executor != null) executor.accept(item, event);
        }

    }
}
