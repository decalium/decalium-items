package me.gepronix.decaliumcustomitems.example;

import com.destroystokyo.paper.ParticleBuilder;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.Timed;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.MetadataHolder;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.time.Duration;

import static net.kyori.adventure.text.Component.text;

public class Katana implements BuildableItem {


	@Override
	public Item build(ConfigurationNode node) throws SerializationException {
		int cooldown = node.node("cooldown").getInt();
		return new ConfigItem(node).create().metaFactory(CooldownableMeta.Factory.INSTANCE)
				.listener(ItemEventTrigger.PLAYER_INTERACT, new Timed<>(this::onInteract, Duration.ofSeconds(cooldown))).build();
	}

    public boolean onInteract(PlayerInteractEvent e, ItemTriggerContext context) {
        if (e.getAction().toString().startsWith("RIGHT_CLICK") /*&&
                pl.getPower().remove(20)*/) {
            doAbility(e.getPlayer());
            return true;
        }
        return false;
    }

    private void doAbility(Player p) {
        Location head = p.getEyeLocation();
        Vector look = head.getDirection().normalize().multiply(3);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
        p.setVelocity(look);
        Location particle = head.add(head.getDirection().multiply(1.2));
        new ParticleBuilder(Particle.SWEEP_ATTACK).location(particle).count(1).allPlayers().spawn();
    }



}

