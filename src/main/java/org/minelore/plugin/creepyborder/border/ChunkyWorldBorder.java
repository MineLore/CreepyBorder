package org.minelore.plugin.creepyborder.border;

import org.bukkit.Location;
import org.minelore.plugin.creepyborder.util.ChunkyBorderUtil;
import org.popcraft.chunky.shape.Shape;

/**
 * @author TheDiVaZo
 * created on 09.02.2025
 */
public class ChunkyWorldBorder implements Border {
    private final String worldName;
    private final Shape shape;

    public ChunkyWorldBorder(String worldName, Shape shape) {
        this.worldName = worldName;
        this.shape = shape;
    }

    @Override
    public double distToBorder(Location location) {
        return ChunkyBorderUtil.distToBorder(location, shape);
    }

    @Override
    public String getWorldName() {
        return worldName;
    }
}
