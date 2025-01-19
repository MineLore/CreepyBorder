package org.minelore.plugin.creepyborder;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    private final Logger LOG = getLogger();

    @Override
    public void onDisable() {
        super.onDisable();
        LOG.info("CreepyBorder plugin is disabled! Development by TheDiVaZo. For MineLoreSMP");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        LOG.info("CreepyBorder plugin is enabled! Development by TheDiVaZo. For MineLoreSMP");

    }
}
