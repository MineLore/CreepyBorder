package org.minelore.plugin.creepyborder.config;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public interface MainConfig {
    String getReloadPermission();
    String getImmunityPermission();
    List<HandlerConfig> getWrapperConfigs();

}
