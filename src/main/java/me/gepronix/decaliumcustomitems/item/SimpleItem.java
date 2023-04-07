package me.gepronix.decaliumcustomitems.item;

import me.gepronix.decaliumcustomitems.event.EventTrigger;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolder;
import me.gepronix.decaliumcustomitems.event.EventTriggerHolderImpl;
import me.gepronix.decaliumcustomitems.event.Triggerable;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class SimpleItem implements Triggerable, MetadataHolder, Item {

    private final EventTriggerHolder triggerHolder;
    private final NamespacedKey key;
    private final ItemModifier modifier;
    private final ItemMetaFactory<?> metaFactory;
    private final Material original;


    public SimpleItem(@NotNull NamespacedKey key, @NotNull Material original, @NotNull ItemModifier modifier,
                      EventTriggerHolder eventTriggerHolder, @NotNull ItemMetaFactory<?> metaFactory) {
        this.key = key;
        this.original = original;
        this.modifier = modifier;
        this.metaFactory = metaFactory;
        this.triggerHolder = eventTriggerHolder;
    }

    public @NotNull NamespacedKey key() {
        return key;
    }

    @Override
    public @NotNull Material original() {
        return this.original;
    }

    @Override
    public @NotNull ItemModifier modifier() {
        return this.modifier;
    }


    public @NotNull Material getOriginal() {
        return original;
    }

    public static Builder builder(Material original) {
        return new Builder(original);
    }

    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return triggerHolder;
    }

    @Override
    public ItemMetaFactory<? extends CustomMeta> metaFactory() {
        return this.metaFactory;
    }

    public static class Builder {
        private final Material original;
        private ItemModifier modifier;
        private NamespacedKey key;
        private ItemMetaFactory<?> metaFactory = CooldownableMeta.Factory.INSTANCE;
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

        public Builder metaFactory(ItemMetaFactory<?> metaFactory) {
            this.metaFactory = metaFactory;
            return this;
        }


        public SimpleItem build() {
            return new SimpleItem(key, original, modifier, eventTriggerHolder, metaFactory);
        }
    }


}