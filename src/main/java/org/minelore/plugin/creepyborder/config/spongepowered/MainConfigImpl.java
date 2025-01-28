package org.minelore.plugin.creepyborder.config.spongepowered;

import org.minelore.plugin.creepyborder.config.MainConfig;
import org.minelore.plugin.creepyborder.config.WrapperConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class MainConfigImpl implements MainConfig {
    private String immunityPermission = "creepyborder.immunity";
    private List<WrapperConfig> wrapperConfigs = List.of(
            new WSoundConfigImpl(100),
            new WBiomeConfigImpl(50),
            new WMagmaGrabConfigImpl(0.1, 20),
            new WMagmaGrabConfigImpl(0.7, 10),
            new WPotionEffectConfigImpl(10),
            new WTimedKillConfigImpl(10)
            );

    @Override
    public String getImmunityPermission() {
        return immunityPermission;
    }

    @Override
    public List<WrapperConfig> getWrapperConfigs() {
        return wrapperConfigs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MainConfigImpl{");
        sb.append("immunityPermission='").append(immunityPermission).append('\'');
        sb.append(", wrapperConfigs=").append(wrapperConfigs);
        sb.append('}');
        return sb.toString();
    }
}
