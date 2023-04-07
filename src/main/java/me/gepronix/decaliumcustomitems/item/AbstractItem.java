package me.gepronix.decaliumcustomitems.item;

import me.gepronix.decaliumcustomitems.event.EventTrigger;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolder;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolderImpl;
import me.gepronix.decaliumcustomitems.event.Triggerable;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * A class that implements basic item methods, made for reducing boilerplate.
 *
 * @author gepron1x
 */
public abstract class AbstractItem implements Item, Triggerable {
    private final EventTriggerHolder triggerHolder = new EventTriggerHolderImpl();
    private ItemModifier modifier = ItemModifierImpl.builder().build();
    private final NamespacedKey key;
    private final Material material;

    /**
     * @param key      - item key
     * @param material - original material
     */
    public AbstractItem(@NotNull NamespacedKey key, @NotNull Material material) {
        this.key = key;
        this.material = material;
    }

    /**
     * Sets ItemModifier of this item.
     *
     * @param modifier - modifier to set
     */
    public void setModifier(@NotNull ItemModifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Adds listener for trigger
     *
     * @param trigger  - Event Trigger
     * @param executor - code that will be executed
     * @param <E>      - event
     * @param <T>      - trigger context
     */
    public <E extends Event, T> void listener(@NotNull EventTrigger<E, T> trigger, @NotNull BiConsumer<E, T> executor) {
        eventTriggerHolder().addListener(trigger, executor);
    }

    @Override
    public String toString() {
        return "item [" + key().asString() + "]";
    }

    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return triggerHolder;
    }

    @NotNull
    @Override
    public NamespacedKey key() {
        return key;
    }

    @NotNull
    @Override
    public Material original() {
        return material;
    }

    @NotNull
    @Override
    public ItemModifier modifier() {
        return modifier;
    }
}
