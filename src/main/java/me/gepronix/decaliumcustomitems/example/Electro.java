/*
 * decalium-items
 * Copyright © 2022 George Pronyuk <https://vk.com/gpronyuk>
 *
 * decalium-items is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * decalium-items is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with decalium-items. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package me.gepronix.decaliumcustomitems.example;

import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.Timed;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.event.TriggerContext;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.utils.AttributeModifierBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Optional;

public final class Electro implements BuildableItem.NoConfig {
    private final Plugin plugin;

    public Electro(Plugin plugin) {

        this.plugin = plugin;
    }
    @Override
    public Item build() {
        return SimpleItem.builder(Material.NETHERITE_SHOVEL).key(new NamespacedKey(plugin, "electro"))
                .modifier(ItemModifierImpl.builder().name(Component.text("Посох")).customModelData(777)
                        .modifier(item -> {
                            item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
                        }).attributeModifiers(new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_SPEED)
                                .slot(EquipmentSlot.HAND).amount(1.2).build(), new AttributeModifierBuilder(Attribute.GENERIC_ATTACK_DAMAGE)
                                .slot(EquipmentSlot.HAND).amount(10).build()).build())
                .listener(ItemEventTrigger.PLAYER_INTERACT, new Timed<>(this::doAbility, Duration.ofSeconds(3)))
                .build();
    }

    private boolean doAbility(PlayerInteractEvent event, ItemTriggerContext context) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return false;
        event.setCancelled(true);

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location head = player.getEyeLocation();
        Vector direction = head.getDirection();
        RayTraceResult raytrace = world.rayTrace(head, direction, 15, FluidCollisionMode.NEVER,true,0.3, (pl) -> !pl.equals(player));
        if(raytrace == null) return false;
        Block block = raytrace.getHitBlock();
        Location location;
        if(raytrace.getHitEntity() instanceof LivingEntity entity) {
            location = entity.getLocation();
        } else if(block != null) {
            location = block.getLocation();
        } else {
            return false;
        }
        world.spawn(location, LightningStrike.class);
        return true;
    }
}
