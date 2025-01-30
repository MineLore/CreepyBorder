package org.minelore.plugin.creepyborder.nms;

import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.minelore.plugin.creepyborder.util.SpecialEffectsData;

/**
 * @author TheDiVaZo
 * created on 30.01.2025
 */
public interface BiomeWrapper {
    NamespacedKey getNamespacedKey();

    void replaceBiome(SpecialEffectsData specialEffectsData);

    void registerBiome();

    Biome getBiome();
}
