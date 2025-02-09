package org.minelore.plugin.creepyborder.config.spongepowered;

import org.bukkit.Color;
import org.minelore.plugin.creepyborder.handler.BiomeHandler;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HBiomeConfig extends HandlerConfig {
    private Color waterColor = Color.RED;
    private Color skyColor = Color.fromBGR(0x4b0082);
    private Color fogColor = Color.fromBGR(0x8b0000);
    private Color waterFogColor = Color.fromBGR(0x8b0000);

    public HBiomeConfig() {
        super(BiomeHandler.NAME);
    }

    public HBiomeConfig(double distToBorder) {
        this();
        this.distToBorder = distToBorder;
    }

    public Color getWaterColor() {
        return waterColor;
    }

    public Color getSkyColor() {
        return skyColor;
    }

    public Color getFogColor() {
        return fogColor;
    }

    public Color getWaterFogColor() {
        return waterFogColor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WBiomeConfigImpl{");
        sb.append("waterColor=").append(waterColor);
        sb.append(", skyColor=").append(skyColor);
        sb.append(", fogColor=").append(fogColor);
        sb.append(", waterFogColor=").append(waterFogColor);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
