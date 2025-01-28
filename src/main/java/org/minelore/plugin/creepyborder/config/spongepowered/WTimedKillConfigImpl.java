package org.minelore.plugin.creepyborder.config.spongepowered;

import org.minelore.plugin.creepyborder.component.TimedKillWrapper;
import org.minelore.plugin.creepyborder.config.WTimedKillConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class WTimedKillConfigImpl extends WrappedConfigImpl implements WTimedKillConfig {
    private int tickToKill = 20 * 10; // 20 ticks -> 1 second

    public WTimedKillConfigImpl() {
        super(TimedKillWrapper.NAME);
    }

    public WTimedKillConfigImpl(double distToBorder) {
        this();
        this.distToBorder = distToBorder;
    }

    @Override
    public int getTickToKill() {
        return tickToKill;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WTimedKillConfigImpl{");
        sb.append("tickToKill=").append(tickToKill);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
