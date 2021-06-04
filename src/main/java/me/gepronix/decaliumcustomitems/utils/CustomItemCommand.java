package me.gepronix.decaliumcustomitems.utils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import me.gepronix.decaliumcustomitems.ItemRegistry;
import me.gepronix.decaliumcustomitems.item.WrappedStack;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

@CommandAlias("ci")
public class CustomItemCommand extends BaseCommand {
    private final ItemRegistry registry;
    public CustomItemCommand(ItemRegistry registry) {
        this.registry = registry;

    }
    @Default
    @CommandCompletion("@items")
    @CommandPermission("decaliumcustomitems.commands.customitem")
    @Syntax("<item> <amount> <player>")
    public void handle(Player player, String name, @Default("1") Integer amount, @Optional Player recipient) {
        Player target = recipient == null ? player : recipient;
        NamespacedKey key = NamespacedKey.fromString(name);
        registry.of(key).map(item -> new WrappedStack(item, amount))
                .map(WrappedStack::get)
                .ifPresent(target.getInventory()::addItem);

    }
}
