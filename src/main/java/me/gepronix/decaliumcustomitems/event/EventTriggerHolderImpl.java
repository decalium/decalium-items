package me.gepronix.decaliumcustomitems.event;

import org.bukkit.event.Event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class EventTriggerHolderImpl implements EventTriggerHolder {
    private final Map<EventTrigger<?, ?>, BiConsumer<?, ?>> eventExecutors = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event, T> Optional<BiConsumer<E, T>> executor(EventTrigger<E, T> trigger) {
        return Optional.ofNullable((BiConsumer<E, T>) eventExecutors.get(trigger));
    }

    @Override
    public Map<EventTrigger<?, ?>, BiConsumer<?, ?>> executors() {
        return Collections.unmodifiableMap(eventExecutors);
    }

    @Override
    public <E extends Event, T> void addListener(EventTrigger<E, T> trigger, BiConsumer<E, T> executor) {
        eventExecutors.put(trigger, executor);
    }

    @Override
    public void removeListener(EventTrigger<?, ?> trigger) {
        eventExecutors.remove(trigger);
    }

    @Override
    public void clear() {
        eventExecutors.clear();
    }
}
