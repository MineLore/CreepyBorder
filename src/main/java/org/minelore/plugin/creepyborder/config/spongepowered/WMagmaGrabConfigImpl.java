package org.minelore.plugin.creepyborder.config.spongepowered;

import org.bukkit.Color;
import org.minelore.plugin.creepyborder.component.MagmaGrabWrapper;
import org.minelore.plugin.creepyborder.config.WBiomeConfig;
import org.minelore.plugin.creepyborder.config.WMagmaGrabConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class WMagmaGrabConfigImpl extends WrappedConfigImpl implements WMagmaGrabConfig {
    private double vectorLength;

    public WMagmaGrabConfigImpl() {
        super(MagmaGrabWrapper.NAME);
    }

    public WMagmaGrabConfigImpl(double vectorLength) {
        this();
        this.vectorLength = vectorLength;
    }

    public WMagmaGrabConfigImpl(double vectorLength, double distToBorder) {
        this(vectorLength);
        this.distToBorder = distToBorder;
    }

    @Override
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
