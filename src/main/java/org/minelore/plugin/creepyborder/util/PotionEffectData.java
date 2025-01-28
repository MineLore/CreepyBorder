package org.minelore.plugin.creepyborder.util;

import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public record PotionEffectData(PotionEffectType type, int amplifier) {
}
