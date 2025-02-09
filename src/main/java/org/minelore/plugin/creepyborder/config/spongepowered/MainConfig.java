package org.minelore.plugin.creepyborder.config.spongepowered;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class MainConfig {
    public MainConfig() {
    }
    private String reloadPermission = "creepyborder.reload";
    private String immunityPermission = "creepyborder.immunity";
    private List<HandlerConfig> handlerConfigs = List.of(
            new HSoundConfig(100),
            new HBiomeConfig(50),
            new HMagmaGrabConfig(0.1, 20),
            new HMagmaGrabConfig(0.7, 10),
            new HPotionEffectConfig(10),
            new HTimedKillConfig(10)
            );

    public String getImmunityPermission() {
        return immunityPermission;
    }

    public String getReloadPermission() { return reloadPermission; }

    public List<HandlerConfig> getWrapperConfigs() {
        return handlerConfigs;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("MainConfigImpl{");
        sb.append("immunityPermission='").append(immunityPermission).append('\'');
        sb.append(", wrapperConfigs=").append(handlerConfigs);
        sb.append('}');
        return sb.toString();
    }
}
