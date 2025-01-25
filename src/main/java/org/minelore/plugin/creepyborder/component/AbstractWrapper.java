package org.minelore.plugin.creepyborder.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

import java.util.*;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public abstract class AbstractWrapper implements Wrapper {
    private static final int OPTIMAL_CAPACITY = 4;

    protected final Set<Player> activePlayer = new HashSet<>(OPTIMAL_CAPACITY);
    private BukkitTask task;
    protected final CreepyBorder plugin;
    protected AbstractWrapper(CreepyBorder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        if (task != null) stop();
        task = runTask();
    }

    @Override
    public void stop() {
        task.cancel();
    }

    @Override
    public void interact(Player player) {
        activePlayer.add(player);
    }

    @Override
    public void cancel(Player player) {
        activePlayer.remove(player);
    }

    protected abstract BukkitTask runTask();
}
