package org.minelore.plugin.creepyborder.config;

import org.bukkit.Sound;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public interface WSoundConfig extends WrapperConfig {
    List<Sound> getSounds();
    int getPitch();
    int getVolume();
    int getPeriodInTick();
}
