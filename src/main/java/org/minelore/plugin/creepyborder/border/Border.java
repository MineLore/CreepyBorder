package org.minelore.plugin.creepyborder.border;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.popcraft.chunkyborder.BorderData;
import org.popcraft.chunkyborder.ChunkyBorder;

import java.util.Optional;

/**
 * @author TheDiVaZo
 * created on 09.02.2025
 */
public interface Border {
    double distToBorder(Location location);

    String getWorldName();

    static Border createBukkitBorder(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("No world found with the name " + worldName);
        }
        return new BukkitWorldBorder(worldName, world.getWorldBorder());
    }

    static Border createChunkyBorder(String worldName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("ChunkyBorder")) {
            throw new IllegalStateException("ChunkyBorder plugin is not enabled");
        }
        RegisteredServiceProvider<ChunkyBorder> chunkyBorder = Bukkit.getServer().getServicesManager().getRegistration(ChunkyBorder.class);
        if (chunkyBorder == null) {
            throw new IllegalStateException("ChunkyBorder has not found of enabled plugin");
        }
        Optional<BorderData> border = chunkyBorder.getProvider().getBorder(worldName);
        if (border.isEmpty()) {
            throw new IllegalStateException("ChunkyBorder has not found border for world " + worldName);
        }
        return new ChunkyWorldBorder(worldName, border.get().getBorder());
    }
}
