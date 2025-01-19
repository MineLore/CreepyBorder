package org.minelore.plugin.creepyborder.manager;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author TheDiVaZo
 * created on 20.01.2025
 */
public interface SoundPlayer {
    void playSound(Sound sound, Player player);

    void playSound(Sound sound, Player player, int pitch, int volume);
}
