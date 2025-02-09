package org.minelore.plugin.creepyborder.config.spongepowered;

import org.minelore.plugin.creepyborder.handler.TimedKillHandler;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HTimedKillConfig extends HandlerConfig {
    private int tickToKill = 20 * 10; // 20 ticks -> 1 second

    public HTimedKillConfig() {
        super(TimedKillHandler.NAME);
    }

    public HTimedKillConfig(double distToBorder) {
        this();
        this.distToBorder = distToBorder;
    }

    public int getTickToKill() {
        return tickToKill;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("WTimedKillConfigImpl{");
        sb.append("tickToKill=").append(tickToKill);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
