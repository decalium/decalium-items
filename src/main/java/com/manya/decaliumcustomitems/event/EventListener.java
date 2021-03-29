package com.manya.decaliumcustomitems.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public interface EventListener<T extends Event> extends Listener {
    @EventHandler
    void handle(T event);
}
