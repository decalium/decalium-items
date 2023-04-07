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
package me.gepronix.decaliumcustomitems.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;


public final class StrafeJumpListener implements Listener {

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Vector vector = event.getTo().subtract(event.getFrom()).toVector();
        Vector look = event.getPlayer().getPlayer().getEyeLocation().getDirection();
        event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(vector.add(look)));



    }

}
