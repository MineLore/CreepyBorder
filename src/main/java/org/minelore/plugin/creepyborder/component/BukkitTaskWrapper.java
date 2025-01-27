package org.minelore.plugin.creepyborder.component;

import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public abstract class BukkitTaskWrapper extends AbstractWrapper {

    private BukkitTask task;

    protected BukkitTaskWrapper(CreepyBorder plugin, String name) {
        super(plugin, name);
    }

    @Override
    public void start() {
        if (task != null) stop();
        task = runTask();
    }

    @Override
    public void stop() {
        if (task == null) return;
        task.cancel();
    }

    protected abstract BukkitTask runTask();
}
