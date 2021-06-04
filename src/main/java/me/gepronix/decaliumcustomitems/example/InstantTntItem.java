package me.gepronix.decaliumcustomitems.example;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.manya.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.kyori.adventure.text.Component.*;

public class InstantTntItem implements Item, Triggerable {
    private final NamespacedKey key;
    private static final PlayerProfile HEAD_PROFILE;
    private final Material original = Material.PLAYER_HEAD;
    private final EventTriggerHolder triggerHolder = new EventTriggerHolderImpl();
    private final ItemModifier modifier;
    public InstantTntItem(Plugin plugin) {
        this.key = new NamespacedKey(plugin, "instant_tnt");
        this.modifier = ItemModifierImpl.builder()
                .name(text("Мгновенный динамит")
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.WHITE))
                .modifier(item -> {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setPlayerProfile(HEAD_PROFILE);
                    item.setItemMeta(meta);
                })
                .visual(
                        ((entity, itemStack) -> {
                            itemStack.addUnsafeEnchantment(Enchantment.MENDING, 1);
                            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        }),
                        ((entity, itemStack) -> {
                            itemStack.removeEnchantment(Enchantment.MENDING);
                            itemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                        })
                ).build();
        triggerHolder.addListener(ItemEventTrigger.BLOCK_PLACE, this::onBlockPlace);


    }
    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return triggerHolder;
    }

    @NotNull
    @Override
    public NamespacedKey key() {
        return key;
    }

    @NotNull
    @Override
    public Material original() {
        return original;
    }

    @NotNull
    @Override
    public ItemModifier modifier() {
        return modifier;
    }
    private void onBlockPlace(BlockPlaceEvent event, ItemTriggerContext context) {
        Location blockLocation = event.getBlock().getLocation();
        blockLocation.getWorld().createExplosion(blockLocation, 8, false, false, event.getPlayer());

    }
    static {
        HEAD_PROFILE = Bukkit.createProfile(UUID.randomUUID());
        HEAD_PROFILE.setProperty(new ProfileProperty("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIxMTY1NzAzOGRkYWZmZGQ5MWI2OGQxYTQ4NWUzY2RiZDVjNTU3NDdkZTMxM2Y4NzYyYThiMzFiZmE2NzY2In19fQ=="));
        HEAD_PROFILE.complete(true);
    }
}
