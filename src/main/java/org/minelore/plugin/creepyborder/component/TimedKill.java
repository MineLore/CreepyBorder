package org.minelore.plugin.creepyborder.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class TimedKill extends BukkitTaskWrapper {
    private final int tickToKill;

    public TimedKill(CreepyBorder plugin, int tickToKill) {
        super(plugin, "TimedKill");
        this.tickToKill = tickToKill;
    }

    @Override
    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : activePlayer) {
                player.damage(player.getHealth());
            }
        }, tickToKill);
    }
}
