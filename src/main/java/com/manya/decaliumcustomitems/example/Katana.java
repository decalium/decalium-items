package com.manya.decaliumcustomitems.example;

import com.manya.decaliumcustomitems.event.EquipmentEventListener;
import com.manya.decaliumcustomitems.item.WrappedStack;
import com.manya.decaliumcustomitems.item.Item;
import com.manya.decaliumcustomitems.item.MetadataHolder;
import com.manya.decaliumcustomitems.item.meta.CooldownableMeta;
import com.manya.decaliumcustomitems.item.meta.ItemMetaFactory;
import com.manya.decaliumcustomitems.item.modifier.DefaultModifier;
import com.manya.decaliumcustomitems.listener.Selectable;
import com.manya.decaliumcustomitems.utils.AttributeModifierBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Katana extends Item implements MetadataHolder, Selectable {
    public Katana() {

        super(Material.IRON_SWORD,
                DefaultModifier.builder()
                .name(Component.text("Katana").decoration(TextDecoration.ITALIC, false))
                .customModelData(313)
                .attributeModifiers(
                        new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_DAMAGE).value(3).build(),
                        new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_SPEED).value(1.6).build()
                ).build()

        );
        super.addListener(EquipmentEventListener.PLAYER_INTERACT, this::onInteract, EquipmentSlot.HAND);

    }
    public void onInteract(ItemStack item, PlayerInteractEvent e) {
       // RpgPlayer pl = RpgPlayer.get(e.getPlayer());
        WrappedStack stack = WrappedStack.asCustomStack(e.getPlayer().getEquipment().getItem(EquipmentSlot.HAND));
        CooldownableMeta meta = (CooldownableMeta) stack.getCustomMeta();
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
    public void onSelect(PlayerItemHeldEvent event) {
        event.getPlayer().sendMessage(Component.text("selected."));
    }

    @Override
    public void onUnSelect(PlayerItemHeldEvent event) {
        event.getPlayer().sendMessage(Component.text("unselected."));
    }

    @Override
    public ItemMetaFactory metaFactory() {
        return CooldownableMeta.Factory.INSTANCE;
    }
}

