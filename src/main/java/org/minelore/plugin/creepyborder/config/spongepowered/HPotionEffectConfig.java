package org.minelore.plugin.creepyborder.config.spongepowered;

import org.bukkit.potion.PotionEffectType;
import org.minelore.plugin.creepyborder.handler.PotionEffectHandler;
import org.minelore.plugin.creepyborder.util.PotionEffectData;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HPotionEffectConfig extends HandlerConfig {
    private Set<PotionEffectData> potionEffectDatas = Set.of(
            new PotionEffectData(PotionEffectType.SLOWNESS, 2),
            new PotionEffectData(PotionEffectType.BLINDNESS, 0),
            new PotionEffectData(PotionEffectType.NAUSEA, 2)
    );

    public HPotionEffectConfig() {
        super(PotionEffectHandler.NAME);
    }

    public HPotionEffectConfig(double distToBorder) {
        this();
        this.distToBorder = distToBorder;
    }

    public Set<PotionEffectData> getEffectDatas() {
        return potionEffectDatas;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WPotionEffectConfigImpl{");
        sb.append("potionEffectDatas=").append(potionEffectDatas);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
