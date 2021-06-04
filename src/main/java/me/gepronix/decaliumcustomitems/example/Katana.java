package me.gepronix.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.event.*;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.WrappedStack;
import me.gepronix.decaliumcustomitems.item.MetadataHolder;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;
import me.gepronix.decaliumcustomitems.item.meta.CustomMeta;
import me.gepronix.decaliumcustomitems.item.meta.ItemMetaFactory;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifier;
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

import static net.kyori.adventure.text.Component.text;

public class Katana implements Item, MetadataHolder, Triggerable {
    private final NamespacedKey key;
    private final Material original;
    private final ItemModifier modifier;
    private final EventTriggerHolder eventTriggerHolder = new EventTriggerHolderImpl();
    public Katana(Plugin plugin) {

        key = new NamespacedKey(plugin, "katana_sword");
        original = Material.IRON_SWORD;
        modifier =  ItemModifierImpl.builder()
                .name(text("Katana").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.WHITE))
                .attributeModifiers(new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_DAMAGE).amount(4).build())
                .customModelData(313).build();
        eventTriggerHolder.addListener(ItemEventTrigger.PLAYER_INTERACT, this::onInteract);

    }
    @NotNull
    @Override
    public EventTriggerHolder eventTriggerHolder() {
        return eventTriggerHolder;
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

    public void onInteract(PlayerInteractEvent e, ItemTriggerContext context) {
       // RpgPlayer pl = RpgPlayer.get(e.getPlayer());
        WrappedStack stack = WrappedStack.of(context.getItemStack());
        CooldownableMeta meta = stack.getCustomMeta()
                .map(CooldownableMeta.class::cast)
                .orElse((CooldownableMeta) metaFactory().createDefault());
        System.out.println("that works!" + meta.lastUsage());
        if(System.currentTimeMillis() - meta.lastUsage() < 5 * 1000) return;
        if(e.getAction().toString().startsWith("RIGHT_CLICK") /*&&
                pl.getPower().remove(20)*/) {
                doAbility(e.getPlayer());
                meta.setLastUsage(System.currentTimeMillis());
        }
        stack.setCustomMeta(meta);
    }

    private void doAbility(Player p) {
            Location head = p.getEyeLocation();
            Vector look = head.getDirection().normalize().multiply(3);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            p.setVelocity(look);
            Location particle = head.add(head.getDirection().multiply(1.2));
            p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particle.getX(), particle.getY(), particle.getZ(), 2);
    }


    @Override
    public ItemMetaFactory<? extends CustomMeta> metaFactory() {
        return CooldownableMeta.Factory.INSTANCE;
    }


}

