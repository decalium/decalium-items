package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.item.WrappedStack;
import com.manya.decaliumcustomitems.item.Interactable;
import com.manya.decaliumcustomitems.utils.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PersonalTeleporter extends Item implements Interactable {
    private final static NamespacedKey LOCATION = DataType.key("location");
    public PersonalTeleporter() {
        super(Material.DIAMOND, DefaultModifier.builder().name(Component.text("teleporter")).build());
    }
    @Override
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        WrappedStack stack = WrappedStack.asCustomStack(item);
        ItemMeta meta = item.getItemMeta();
        e.setCancelled(true);
        Player p = e.getPlayer();

        switch(e.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_BLOCKS,1,1);
                break;
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                p.sendActionBar("Локация успешно сохранена.");
                break;
        }

    }


}
