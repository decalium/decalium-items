package me.gepronix.decaliumcustomitems.event;

import org.bukkit.event.Event;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface EventTriggerHolder {
    <E extends Event, T> Optional<BiConsumer<E, T>> executor(EventTrigger<E, T> trigger);
    Map<EventTrigger<?, ?>, BiConsumer<?, ?>> executors();
    <E extends Event, T> void addListener(EventTrigger<E, T> trigger, BiConsumer<E, T> executor);
    void removeListener(EventTrigger<?, ?> trigger);
    void clear();
}
