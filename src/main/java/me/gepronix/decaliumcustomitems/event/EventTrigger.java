package me.gepronix.decaliumcustomitems.event;

import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface EventTrigger<E extends Event, T> extends Listener {

    @EventHandler
    Map<Triggerable, T> handle(E event);

    Class<E> getEventClass();

    @SuppressWarnings("unchecked")
    default void register(Plugin plugin, EventPriority priority) {
        plugin.getServer()
                .getPluginManager()
                .registerEvent(
                        getEventClass(),
                        this,
                        priority,
                        (l, e) -> {
                            if (!e.getClass().equals(getEventClass())) return;
                            E event = (E) e;
                            handle(event).forEach((triggerable, context) ->
                                    triggerable.eventTriggerHolder()
                                            .executor(this)
                                            .ifPresent(executor -> executor.accept(event, context))
                            );
                        },
                        plugin
                );

    }

    default void register(Plugin plugin) {
        register(plugin, EventPriority.NORMAL);
    }

    default void unregister() {
        HandlerList.unregisterAll(this);
    }


}
