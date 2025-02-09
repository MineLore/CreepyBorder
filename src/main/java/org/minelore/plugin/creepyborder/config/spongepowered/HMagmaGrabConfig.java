package org.minelore.plugin.creepyborder.config.spongepowered;

import org.minelore.plugin.creepyborder.handler.MagmaGrabHandler;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HMagmaGrabConfig extends HandlerConfig {
    private double vectorLength;

    public HMagmaGrabConfig() {
        super(MagmaGrabHandler.NAME);
    }

    public HMagmaGrabConfig(double vectorLength) {
        this();
        this.vectorLength = vectorLength;
    }

    public HMagmaGrabConfig(double vectorLength, double distToBorder) {
        this(vectorLength);
        this.distToBorder = distToBorder;
    }

    public double getVectorLength() {
        return vectorLength;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WMagmaGrabConfigImpl{");
        sb.append("vectorLength=").append(vectorLength);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
