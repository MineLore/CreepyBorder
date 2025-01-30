package org.minelore.plugin.creepyborder.handler;

import org.bukkit.entity.Player;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public interface Handler {
    String getName();

    void start();
    void stop();
    void interact(Player player);
    void cancel(Player player);
}
