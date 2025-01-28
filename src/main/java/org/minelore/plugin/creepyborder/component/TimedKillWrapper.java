package org.minelore.plugin.creepyborder.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class TimedKillWrapper extends BukkitTaskWrapper {
    public static final String NAME = "TimedKill";

    private final int tickToKill;

    public TimedKillWrapper(CreepyBorder plugin, int tickToKill) {
        super(plugin, NAME);
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
