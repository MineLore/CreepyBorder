package org.minelore.plugin.creepyborder.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public interface HandlerConfig {
    String getName();
    double getDistToBorder();
}
