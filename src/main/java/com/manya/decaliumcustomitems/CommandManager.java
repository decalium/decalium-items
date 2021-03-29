package com.manya.decaliumcustomitems;

import com.google.gson.Gson;
import com.manya.decaliumcustomitems.item.CustomMaterial;
import com.manya.decaliumcustomitems.item.WrappedStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if(args[0].equalsIgnoreCase("loretest")) {
			ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
			meta.lore(Arrays.asList(Component.text(args[1])));
			p.getInventory().getItemInMainHand().setItemMeta(meta);
		}
		else if(args[0].equalsIgnoreCase("test")) {
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
			ItemMeta meta = item.getItemMeta();

			meta.setDisplayName("&8[&c&lBERSERK&8]");




			meta.setCustomModelData(2238);

			PersistentDataContainer container = meta.getPersistentDataContainer();
			container.set(new NamespacedKey(DecaliumCustomItems.get(), "special"), PersistentDataType.STRING, "berserk");

			AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", -3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
			item.setItemMeta(meta);
			((Player) sender).getInventory().addItem(item);

		} else {
			p.getInventory().addItem(new WrappedStack(CustomMaterial.of(args[0])).get());
		}


		  /*ItemMeta meta = item.getItemMeta();
		  UUID uuid = UUID.randomUUID();
		  NamespacedKey key = new NamespacedKey(AdvancedCustomItems.getInstance(),"test_uuid");
		   p.sendMessage(uuid.toString());
			meta.getPersistentDataContainer().set(key, DataTypes.UUID, uuid);
			item.setItemMeta(meta);
			p.sendMessage(meta.getPersistentDataContainer().get(key, DataTypes.UUID).toString());*/

		return true;
	}
	private String serializeItemStack(ItemStack item) {
		String result = "";
		try {
			Class nmsStack = Class.forName("net.minecraft.server."+ DecaliumCustomItems.getVersion()+".ItemStack");
			Class nbtTagClass = Class.forName("net.minecraft.server."+ DecaliumCustomItems.getVersion()+".NBTTagCompound");
			Object itemz = Class.forName("org.bukkit.craftbukkit."+ DecaliumCustomItems.getVersion()+".inventory.CraftItemStack")
					.getMethod("asNMSCopy", new Class[] {ItemStack.class}).invoke(null, new Object[]{item});
			Object nbtTag = nbtTagClass.getConstructor().newInstance();
			nmsStack.getMethod("save", new Class[] {nbtTagClass}).invoke(itemz, new Object[]{nbtTag});
			Gson gson = new Gson();
			 result = gson.toJson(nbtTag);
			 DecaliumCustomItems.get().send(result);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}
	private ItemStack deserializeItemStack(String json) {
		ItemStack result = null;
		Gson gson = new Gson();
		try {
			Class nbtTagClass = Class.forName("net.minecraft.server."+ DecaliumCustomItems.getVersion()+".NBTTagCompound");
			Object nbtTagCompound = gson.fromJson(json, nbtTagClass);
			Class nmsStack = Class.forName("net.minecraft.server."+ DecaliumCustomItems.getVersion()+".ItemStack");
			Object nmsStackInstance = nmsStack.getMethod("createStack", new Class[] {nbtTagClass}).invoke(null, nbtTagCompound);

			 result = (ItemStack) Class.forName("org.bukkit.craftbukkit."+ DecaliumCustomItems.getVersion()+".inventory.CraftItemStack")
					.getMethod("asBukkitCopy", new Class[] {nmsStack}).invoke(null, new Object[]{nmsStackInstance});

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	}




