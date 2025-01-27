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

    protected final String name;
    protected final Set<Player> activePlayer = new HashSet<>(OPTIMAL_CAPACITY);
    protected final CreepyBorder plugin;
    protected AbstractWrapper(CreepyBorder plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void interact(Player player) {
        activePlayer.add(player);
    }

    @Override
    public void cancel(Player player) {
        activePlayer.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AbstractWrapper that = (AbstractWrapper) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
