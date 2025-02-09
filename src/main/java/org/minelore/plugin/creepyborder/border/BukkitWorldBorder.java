package org.minelore.plugin.creepyborder.border;

import org.bukkit.Location;
import org.bukkit.WorldBorder;

/**
 * @author TheDiVaZo
 * created on 09.02.2025
 */
public class BukkitWorldBorder implements Border{
    private final String worldName;
    private final double centerX;
    private final double centerZ;
    private final double borderRadius;

    public BukkitWorldBorder(String worldName, WorldBorder worldBorder) {
        this.worldName = worldName;
        centerX = worldBorder.getCenter().getX();
        centerZ = worldBorder.getCenter().getZ();
        borderRadius = worldBorder.getSize() / 2;
    }

    @Override
    public double distToBorder(Location location) {
        double x = location.getX();
        double z = location.getZ();
        return Math.min(
                borderRadius - Math.abs(x - centerX),
                borderRadius - Math.abs(z - centerZ)
        );
    }

    @Override
    public String getWorldName() {
        return worldName;
    }
}
