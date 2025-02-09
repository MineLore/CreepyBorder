package org.minelore.plugin.creepyborder.config.spongepowered;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HandlerConfig {
    protected final String name;
    protected double distToBorder;

    public HandlerConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getDistToBorder() {
        return distToBorder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WrappedConfigImpl{");
        sb.append("name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
