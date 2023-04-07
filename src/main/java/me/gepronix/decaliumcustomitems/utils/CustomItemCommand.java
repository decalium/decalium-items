package me.gepronix.decaliumcustomitems.utils;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.ItemRegistry;
import me.gepronix.decaliumcustomitems.item.StackOfItems;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("ci")
public class CustomItemCommand extends BaseCommand {
    private final ItemRegistry registry;
	private final DecaliumCustomItems plugin;

	public CustomItemCommand(ItemRegistry registry, DecaliumCustomItems plugin) {
        this.registry = registry;
		this.plugin = plugin;
	}

    @Default
    @CommandCompletion("@items")
    @CommandPermission("decaliumcustomitems.commands.customitem")
    @Syntax("<item> <amount> <player>")
    public void handle(Player player, String name, @Default("1") Integer amount, @Optional Player recipient) {
        Player target = recipient == null ? player : recipient;
        NamespacedKey key = NamespacedKey.fromString(name);
        registry.of(key).map(item -> new StackOfItems(item, amount))
                .map(StackOfItems::get)
                .ifPresent(target.getInventory()::addItem);

    }

	@Subcommand("reload")
	@CommandPermission("decaliumcustomitems.commands.reload")
	public void reload(CommandSender sender) {
		plugin.disable();
		plugin.enable();
		sender.sendMessage("Reloaded!");
	}
}
