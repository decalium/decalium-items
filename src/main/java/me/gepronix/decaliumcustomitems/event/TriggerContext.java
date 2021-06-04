package me.gepronix.decaliumcustomitems.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public interface TriggerContext<T extends Event> {
    @NotNull T getEvent();
}
