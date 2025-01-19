package org.minelore.plugin.creepyborder.config;

import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.potion.PotionEffect;
import org.minelore.plugin.creepyborder.deatheffect.DeathEffect;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public interface MainConfig {

    interface SoundConfig {
        boolean isEnabled();

        int getDistFromRealBorder();

        List<Sound> getSounds();

        int getPeriodPlaySoundInSecond();
    }

    interface BiomeConfig {
        boolean isEnabled();

        int getDistFromRealBorder();

        Biome getBiome();
    }

    interface PotionEffectConfig {
        boolean isEnabled();

        int getDistFromRealBorder();

        PotionEffect getPotionEffect();
    }

    interface KillConfig {
        boolean isEnabled();

        DeathEffect getDeathEffect();

        int getTimeUntilDeath();

    }

}
