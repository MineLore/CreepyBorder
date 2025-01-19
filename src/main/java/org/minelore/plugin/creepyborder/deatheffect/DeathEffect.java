package org.minelore.plugin.creepyborder.deatheffect;

import org.bukkit.entity.Player;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public interface DeathEffect {
    String getName();

    void startEffect(Player player);

    void stopEffect(Player player);

    void hasEffect(Player player);
}
