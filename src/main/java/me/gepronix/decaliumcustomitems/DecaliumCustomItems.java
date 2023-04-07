package me.gepronix.decaliumcustomitems;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.ParticleBuilder;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItems;
import me.gepronix.decaliumcustomitems.event.EventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.example.*;
import me.gepronix.decaliumcustomitems.example.bag.Bag;
import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.listener.GuiListener;
import me.gepronix.decaliumcustomitems.protocol.ItemListener;
import me.gepronix.decaliumcustomitems.utils.CustomItemCommand;
import me.gepronix.decaliumcustomitems.utils.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class DecaliumCustomItems extends JavaPlugin {
    private static DecaliumCustomItems instance;
    private ItemRegistry itemRegistry;
    private PaperCommandManager commandManager;
    private ItemListener itemListener;
    private final Set<EventTrigger<?, ?>> registeredTriggers = new HashSet<>();
    private ProtocolManager protocolManager;


    public void onEnable() {
        instance = this;
        enable();
    }

	public void enable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		itemRegistry = new ItemRegistryImpl(this);
		//lets register our custom items
		// itemListener = new ItemListener(this, itemRegistry);
		itemRegistry.registerItem(new Bag(this));
		itemRegistry.registerItem(new InstantTntItem(this));
		itemRegistry.registerItem(new RailGun(this));

		itemRegistry.registerItem(SimpleItem.builder(Material.DIAMOND_HOE)
				.modifier(ItemModifierImpl.builder().name(Component.text("Plasmagun")).build())
				.key(DataType.key("plasmagun"))
				.listener(ItemEventTrigger.PLAYER_INTERACT, (event, ctx) -> {
					event.setCancelled(true);
					Player p = event.getPlayer();
					Location headloc = p.getEyeLocation();
					p.getWorld().spawn(headloc.add(headloc.getDirection()), SmallFireball.class, fb -> {
						fb.setShooter(p);
						fb.setVelocity(headloc.getDirection());
					});
					p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 30, 1);
				}).build()
		);

		itemRegistry.registerItem(new Electro(this).build());

		ConfigItems configItems = new ConfigItems(this);
		configItems.loadBuiltins(itemRegistry);
		configItems.loadCustom(itemRegistry);
		// protocolManager.addPacketListener(new ItemListener(this, itemRegistry));
		this.commandManager = new PaperCommandManager(this);
		commandManager.getCommandCompletions().registerCompletion("items", c -> {
			return itemRegistry.keys()
					.stream().map(NamespacedKey::asString).collect(Collectors.toList());
		});
		commandManager.registerCommand(new CustomItemCommand(itemRegistry, this));
		Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
		registerTriggers();
	}

	public void disable() {
		this.commandManager.unregisterCommands();
		HandlerList.unregisterAll(this);
		getServer().getScheduler().cancelTasks(this);
		itemRegistry.clear();
		// protocolManager.removePacketListener(itemListener);
		commandManager.unregisterCommands();

	}

    private void registerTriggers() {
        Arrays.asList(ItemEventTrigger.PLAYER_INTERACT, ItemEventTrigger.BLOCK_PLACE, ItemEventTrigger.DAMAGE, ItemEventTrigger.KNOCKBACK).forEach(trigger -> {
            registeredTriggers.add(trigger);
            trigger.register(this);
        });

    }


    public void onDisable() {
		disable();
    }


    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public static DecaliumCustomItems get() {
        return instance;
    }


}


