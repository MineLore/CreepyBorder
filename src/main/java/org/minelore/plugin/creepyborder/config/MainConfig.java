package org.minelore.plugin.creepyborder.config;

import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.potion.PotionEffect;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public interface MainConfig {
    String getImmunityPermission();
    List<WrapperConfig> getWrapperConfigs();

}
