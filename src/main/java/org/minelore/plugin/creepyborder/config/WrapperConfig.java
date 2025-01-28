package org.minelore.plugin.creepyborder.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public interface WrapperConfig {
    String getName();
    double getDistToBorder();
}
