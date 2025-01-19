package org.minelore.plugin.creepyborder.manager;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

/**
 * @author TheDiVaZo
 * created on 20.01.2025
 */
public interface FakeBiomeSetter {
    void setFakeBiome(Biome biome, Player player);

    void resetFakeBiome(Player player);
}
