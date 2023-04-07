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

import com.manya.pdc.DataTypes;
import me.gepronix.decaliumcustomitems.BuildableItem;
import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import me.gepronix.decaliumcustomitems.Timed;
import me.gepronix.decaliumcustomitems.config.serializer.ConfigItem;
import me.gepronix.decaliumcustomitems.event.ItemEventTrigger;
import me.gepronix.decaliumcustomitems.event.ItemTriggerContext;
import me.gepronix.decaliumcustomitems.item.AbstractItem;
import me.gepronix.decaliumcustomitems.item.Item;
import me.gepronix.decaliumcustomitems.item.SimpleItem;
import me.gepronix.decaliumcustomitems.item.meta.CooldownableMeta;
import me.gepronix.decaliumcustomitems.item.modifier.ItemModifierImpl;
import me.gepronix.decaliumcustomitems.utils.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public final class Cannon implements BuildableItem {

    private final NamespacedKey EXPLODE_INSTANTLY = DataType.key("explode_instantly");
    private final Set<TNTPrimed> dynamite = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public Item build(ConfigurationNode node) throws SerializationException {
		SimpleItem.Builder builder = new ConfigItem(node).create();
        Bukkit.getScheduler().runTaskTimer(DecaliumCustomItems.get(), () -> {
            Iterator<TNTPrimed> iterator = dynamite.iterator();
            while(iterator.hasNext()) {
                TNTPrimed tnt = iterator.next();
                if(!tnt.getPersistentDataContainer().has(EXPLODE_INSTANTLY, DataTypes.BOOLEAN)) continue;
                if(tnt.getFuseTicks() <= 0) {
                    iterator.remove();
                    continue;
                }
                BoundingBox box = tnt.getBoundingBox();
                Location center = box.getCenter().toLocation(tnt.getWorld());
                if(!center.getNearbyLivingEntities(
                        box.getWidthX() * 0.5,
                        box.getHeight() * 0.5,
                        box.getWidthZ() * 0.5,
                        e -> !e.equals(tnt.getSource()) && !e.equals(tnt)).isEmpty()
                ) {
                    tnt.setFuseTicks(0);
                    iterator.remove();
                }
            }

        }, 1, 1);
        return builder.metaFactory(CooldownableMeta.Factory.INSTANCE).listener(ItemEventTrigger.PLAYER_INTERACT, new Timed<>(this::on, Duration.ofSeconds(2))).build();
    }

    private boolean on(PlayerInteractEvent event, ItemTriggerContext ctx) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) return false;
        Player player = event.getPlayer();
        Location headLocation = player.getEyeLocation();
        Vector direction = headLocation.getDirection();
        TNTPrimed primed = player.getWorld().spawn(headLocation.add(direction).add(0, -0.5, 0), TNTPrimed.class, tnt -> {
            tnt.getPersistentDataContainer().set(EXPLODE_INSTANTLY, DataTypes.BOOLEAN, true);
            tnt.setFuseTicks(27);
            // tnt.setGravity(false);
            tnt.setYield(3);
            tnt.setSource(player);
            tnt.setVelocity(direction.multiply(2.8));
        });
        dynamite.add(primed);
        if(player.getGameMode() != GameMode.CREATIVE) ctx.getItemStack().subtract();
        return true;
    }


}
