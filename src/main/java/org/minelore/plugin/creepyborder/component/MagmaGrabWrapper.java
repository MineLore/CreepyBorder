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
public class MagmaGrabWrapper extends AbstractWrapper{
    private final int liteVectorLength;
    private final int strongVectorLength;
    private final Predicate<Player> liteToStrongCondition;

    protected MagmaGrabWrapper(CreepyBorder plugin, int liteVectorLength, int strongVectorLength, Predicate<Player> liteToStrongCondition) {
        super(plugin);
        this.liteVectorLength = liteVectorLength;
        this.strongVectorLength = strongVectorLength;
        this.liteToStrongCondition = liteToStrongCondition;
    }

    @Override
    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : activePlayer) {
                if (!player.isSwimming()) return;
                int vectorLength = liteToStrongCondition.test(player) ? strongVectorLength : liteVectorLength;
                Vector vector = new Vector(0, vectorLength, 0);
                player.setVelocity(vector);
            }
        }, 0, 10);
    }
}
