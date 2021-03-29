package com.manya.decaliumcustomitems;

import com.comphenix.protocol.ProtocolLibrary;
import com.manya.decaliumcustomitems.event.EquipmentEventListener;
import com.manya.decaliumcustomitems.item.CustomMaterial;
import com.manya.decaliumcustomitems.example.bag.Bag;
import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.listener.GuiListener;
import com.manya.decaliumcustomitems.listener.Selectable;
import com.manya.decaliumcustomitems.protocol.ItemListener;
import com.manya.decaliumcustomitems.example.Katana;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;


public class DecaliumCustomItems extends JavaPlugin {
private static DecaliumCustomItems instance;

private CommandManager cmanager;

public void onEnable() {
	instance = this;
	cmanager = new CommandManager();
	send(getVersion());
	//lets register our custom items
	getCommand("customitem").setExecutor(cmanager);
	Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
	//CustomMaterial.registerMaterial("katana_sword", new Katana(Material.IRON_SWORD,4.3,2));
	//CustomMaterial.registerMaterial("personal_teleporter", new PersonalTeleporter(333, Material.DIAMOND));
	/*CustomMaterial.registerMaterial("shaft", new Shaft(209,Material.IRON_PICKAXE));*/
	//CustomMaterial.registerMaterial("railgun", new RailGun(2008, Material.IRON_AXE));
	/*CustomMaterial.registerMaterial("gauss",new Gauss(1,Material.GOLDEN_HOE));
	CustomMaterial.registerMaterial("plasmagun",new PlasmaGun(1,Material.DIAMOND_SWORD));*/
	ProtocolLibrary.getProtocolManager().addPacketListener(new ItemListener(this));
	Selectable.registerListener(this);
	CustomMaterial.registerMaterial("katana_sword", new Katana());
	Item exampleItem = new Item(Material.AIR, DefaultModifier.builder().build());
	exampleItem.addListener(EquipmentEventListener.PLAYER_INTERACT, (i, e) -> e.getPlayer().sendMessage(Component.text("hi")), EquipmentSlot.HAND);

	CustomMaterial.registerMaterial("bag", new Bag());

}
public void onDisable() {
	//such empty
}
public void send(String... message) {
	for(String msg : message) {
		Bukkit.getConsoleSender().sendMessage("§3[AdvancedCustomItems]§r " + msg);
	}

}
public static DecaliumCustomItems get() {
	return instance;
}

	public static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
}


