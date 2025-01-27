package org.minelore.plugin.creepyborder.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.minelore.plugin.creepyborder.CreepyBorder;

import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class MagmaGrabWrapper extends BukkitTaskWrapper {
    private static final String NAME = "MagmaGrab";

    private final double vectorLength;

    public MagmaGrabWrapper(CreepyBorder plugin, double vectorLength) {
        super(plugin, NAME);
        this.vectorLength = vectorLength;
    }

    @Override
    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : activePlayer) {
                if (!player.isSwimming() && player.isInWater()) return;
                Vector vector = new Vector(0, -vectorLength, 0);
                player.setVelocity(vector);
            }
        }, 0, 10);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MagmaGrabWrapper that = (MagmaGrabWrapper) o;
        return name.equals(that.name) && Double.compare(vectorLength, that.vectorLength) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Double.hashCode(vectorLength);
        return result;
    }
}
