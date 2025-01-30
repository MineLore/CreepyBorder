package org.minelore.plugin.creepyborder.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;
import org.minelore.plugin.creepyborder.util.EnableHandlerData;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class BorderManager {
    private final CreepyBorder plugin;
    private final List<EnableHandlerData> enableWrappers;
    private final String successWorldName;
    private BukkitTask bukkitTask;
    private Predicate<Player> immunityCondition;

    private boolean isStarted = false;

    public BorderManager(CreepyBorder plugin, String successWorldName, List<EnableHandlerData> wrappers, Predicate<Player> immunityCondition) {
        this.enableWrappers = List.copyOf(wrappers);
        this.plugin = plugin;
        this.successWorldName = successWorldName;
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
        World world = Bukkit.getWorld(successWorldName);
        WorldBorder border = world.getWorldBorder();
        Location borderCenter = border.getCenter();

        double centerX = borderCenter.getX();
        double centerZ = borderCenter.getZ();
        double borderRadius = border.getSize() / 2;

        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(successWorldName) || immunityCondition.test(player)) return;

                Location location = player.getLocation();
                double playerX = location.getX();
                double playerZ = location.getZ();
                double distanceToBorderSide = Math.min(
                        borderRadius - Math.abs(playerX - centerX),
                        borderRadius - Math.abs(playerZ - centerZ)
                );

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
