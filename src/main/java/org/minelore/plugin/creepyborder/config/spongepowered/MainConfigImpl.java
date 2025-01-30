package org.minelore.plugin.creepyborder.config.spongepowered;

import org.minelore.plugin.creepyborder.config.MainConfig;
import org.minelore.plugin.creepyborder.config.HandlerConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class MainConfigImpl implements MainConfig {
    private String reloadPermission = "creepyborder.reload";
    private String immunityPermission = "creepyborder.immunity";
    private List<HandlerConfig> handlerConfigs = List.of(
            new HSoundConfigImpl(100),
            new HBiomeConfigImpl(50),
            new HMagmaGrabConfigImpl(0.1, 20),
            new HMagmaGrabConfigImpl(0.7, 10),
            new HPotionEffectConfigImpl(10),
            new WTimedKillConfigImpl(10)
            );

    @Override
    public String getImmunityPermission() {
        return immunityPermission;
    }

    @Override
    public String getReloadPermission() { return reloadPermission; }

    @Override
    public List<HandlerConfig> getWrapperConfigs() {
        return handlerConfigs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MainConfigImpl{");
        sb.append("immunityPermission='").append(immunityPermission).append('\'');
        sb.append(", wrapperConfigs=").append(handlerConfigs);
        sb.append('}');
        return sb.toString();
    }
}
