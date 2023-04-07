package me.gepronix.decaliumcustomitems;

import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.StackOfItems;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Timed<E> implements BiConsumer<E, ItemTriggerContext> {

    private final BiFunction<E, ItemTriggerContext, Boolean> consumer;
    private final Duration coolDown;

    public Timed(BiFunction<E, ItemTriggerContext, Boolean> consumer, Duration coolDown) {
        this.consumer = consumer;
        this.coolDown = coolDown;
    }

    @Override
    public void accept(E e, ItemTriggerContext itemTriggerContext) {
        StackOfItems stack = StackOfItems.of(itemTriggerContext.getItemStack());
        CooldownableMeta meta = stack.getCustomMeta()
                .map(CooldownableMeta.class::cast)
                .orElse(CooldownableMeta.Factory.INSTANCE.createDefault());
        if(System.currentTimeMillis() - meta.lastUsage() < coolDown.toMillis()) return;
        if(consumer.apply(e, itemTriggerContext)) meta.setLastUsage(System.currentTimeMillis());
        stack.setCustomMeta(meta);
    }
}

