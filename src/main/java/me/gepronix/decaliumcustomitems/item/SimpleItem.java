package me.gepronix.decaliumcustomitems.item;

import com.manya.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.event.EventTrigger;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolder;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolderImpl;
import me.gepronix.decaliumcustomitems.event.Triggerable;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class SimpleItem implements Triggerable {

    private final EventTriggerHolder triggerHolder;
    private final NamespacedKey key;
    private final ItemModifier modifier;
    private final Material original;

    protected SimpleItem(
            @NotNull NamespacedKey key,
            @NotNull Material original,
            @NotNull ItemModifier modifier,
            @NotNull EventTriggerHolder triggerHolder
    ) {
        this.key = key;
        this.modifier = modifier;
        this.original = original;
        this.triggerHolder = triggerHolder;

    }
    public SimpleItem(@NotNull NamespacedKey key, @NotNull Material original, @NotNull ItemModifier modifier) {
        this.key = key;
        this.original = original;
        this.modifier = modifier;
        this.triggerHolder = new EventTriggerHolderImpl();
    }

    public @NotNull NamespacedKey key() {return key;}



    public @NotNull Material getOriginal() {
        return original;
    }

    public static Builder builder(Material original) {
        return new Builder(original);
    }

    public ItemModifier itemModifier() {
        return modifier;
    }



    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return triggerHolder;
    }

    public static class Builder {
        private final Material original;
        private ItemModifier modifier;
        private NamespacedKey key;
        private final EventTriggerHolder eventTriggerHolder = new EventTriggerHolderImpl();


        private Builder(Material original) {
            this.original = original;
        }

        public Builder modifier(ItemModifier modifier) {
            this.modifier = modifier;
            return this;
        }
        public Builder key(NamespacedKey key) {
            this.key = key;
            return this;
        }
        public <E extends Event, T> Builder listener(EventTrigger<E, T> trigger, BiConsumer<E, T> executor) {
            eventTriggerHolder.addListener(trigger, executor);
            return this;
        }

        public SimpleItem build() {
            return new SimpleItem(key, original, modifier, eventTriggerHolder);
        }
    }





}