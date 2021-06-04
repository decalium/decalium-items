package me.gepronix.decaliumcustomitems;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.gepronix.decaliumcustomitems.event.EventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.example.InstantTntItem;
import me.gepronix.decaliumcustomitems.example.Katana;
import me.gepronix.decaliumcustomitems.example.bag.Bag;
import me.gepronix.decaliumcustomitems.listener.GuiListener;

import me.gepronix.decaliumcustomitems.protocol.ItemListener;
import me.gepronix.decaliumcustomitems.utils.CustomItemCommand;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.bukkit.*;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.lang.reflect.InvocationTargetException;
import java.util.*;
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
	protocolManager = ProtocolLibrary.getProtocolManager();
	itemRegistry = new ItemRegistryImpl(this);
	//lets register our custom items
	itemListener = new ItemListener(this, itemRegistry);
	itemRegistry.registerItem(new Bag(this));
	itemRegistry.registerItem(new Katana(this));
	itemRegistry.registerItem(new InstantTntItem(this));
	itemRegistry.registerItem(new Crystal(this));
	protocolManager.addPacketListener(new ItemListener(this, itemRegistry));
	this.commandManager = new PaperCommandManager(this);
	commandManager.getCommandCompletions().registerCompletion("items", c -> itemRegistry.keys()
			.stream().map(NamespacedKey::asString).collect(Collectors.toList()));
	commandManager.registerCommand(new CustomItemCommand(itemRegistry));
	Bukkit.getPluginManager().registerEvents(new GuiListener(), this);

	PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
	GameProfile profile = new GameProfile(UUID.randomUUID(), "profile");
	try {
		PacketPlayOutPlayerInfo.PlayerInfoData pid =
				PacketPlayOutPlayerInfo.PlayerInfoData.class.getConstructor(GameProfile.class,
						Integer.class,
						EnumGamemode.class,
						IChatBaseComponent.class).newInstance(profile,
						0,
						EnumGamemode.CREATIVE,
						IChatBaseComponent.ChatSerializer.a("profile"));
		PacketPlayOutPlayerInfo.class.getDeclaredField("b").set(packet, pid);
	} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
		e.printStackTrace();
	}
	Player player = null;
	((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	registerTriggers();
;
}
private void registerTriggers() {
	Arrays.asList(ItemEventTrigger.PLAYER_INTERACT, ItemEventTrigger.BLOCK_PLACE).forEach(trigger -> {
		registeredTriggers.add(trigger);
		trigger.register(this);
	});

}
public void onDisable() {
	instance = null;
	itemRegistry.clear();
	protocolManager.removePacketListener(itemListener);
	commandManager.unregisterCommands();
	registeredTriggers.forEach(EventTrigger::unregister);
	registeredTriggers.clear();

}



public ItemRegistry getItemRegistry() {
	return itemRegistry;
}
public static DecaliumCustomItems get() {
	return instance;
}


}


