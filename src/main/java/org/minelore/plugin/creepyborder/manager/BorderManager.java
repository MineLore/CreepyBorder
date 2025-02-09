package org.minelore.plugin.creepyborder.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;
import org.minelore.plugin.creepyborder.border.Border;
import org.minelore.plugin.creepyborder.util.EnableHandlerData;
import org.popcraft.chunkyborder.ChunkyBorder;
import org.popcraft.chunkyborder.util.Particles;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class BorderManager {
    private final CreepyBorder plugin;
    private final List<EnableHandlerData> enableWrappers;
    private BukkitTask bukkitTask;
    private final Predicate<Player> immunityCondition;
    private Border border;

    private boolean isStarted = false;

    public BorderManager(CreepyBorder plugin, Border border, List<EnableHandlerData> wrappers, Predicate<Player> immunityCondition) {
        this.enableWrappers = List.copyOf(wrappers);
        this.plugin = plugin;
        this.border = border;
        this.immunityCondition = immunityCondition;
    }

    public void start() {
        if (isStarted) return;
        isStarted = true;
        if (enableWrappers.isEmpty()) return;
        for (EnableHandlerData enableWrapper : enableWrappers) {
            enableWrapper.handler().start();
        }
        if (bukkitTask != null) bukkitTask.cancel();
        bukkitTask = runTask();
    }

    public void stop() {
        if (!isStarted) return;
        isStarted = false;
        if (enableWrappers.isEmpty()) return;
        for (EnableHandlerData enableWrapper : enableWrappers) {
            enableWrapper.handler().stop();
        }
        if (bukkitTask!= null) bukkitTask.cancel();
    }

    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(border.getWorldName()) || immunityCondition.test(player)) return;

                Location location = player.getLocation();
                double distanceToBorderSide = border.distToBorder(location);

                for (EnableHandlerData enableWrapper : enableWrappers) {
                    double dungeonDistance = enableWrapper.distToBorder();
                    if (distanceToBorderSide < dungeonDistance) {
                        enableWrapper.handler().interact(player);
                    }
                    else {
                        enableWrapper.handler().cancel(player);
                    }
                }
            }
        }, 0, 30);
    }

}
