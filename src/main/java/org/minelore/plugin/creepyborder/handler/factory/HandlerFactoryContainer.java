package org.minelore.plugin.creepyborder.handler.factory;

import org.minelore.plugin.creepyborder.config.spongepowered.HandlerConfig;
import org.minelore.plugin.creepyborder.handler.Handler;

import java.util.Map;
import java.util.Optional;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class HandlerFactoryContainer {
    private final Map<String, HandlerFactory<?, ?>> wrappersFactories;

    public HandlerFactoryContainer(Map<String, HandlerFactory<?, ?>> factories) {
        wrappersFactories = Map.copyOf(factories);
    }

    public <T extends Handler, K extends HandlerConfig> Optional<T> toWrapper(K config) {
        HandlerFactory<T, K> function = (HandlerFactory<T, K>) wrappersFactories.get(config.getName());
        if (function == null) return Optional.empty();
        return Optional.ofNullable(function.loadWrapper(config));
    }

    public <T extends Handler, K extends HandlerConfig> Optional<HandlerFactory<T, K>> getHandlerFactory(String name) {
        return Optional.ofNullable((HandlerFactory<T, K>) wrappersFactories.get(name));
    }

    public boolean containsHandlerFactory(String name) {
        return wrappersFactories.containsKey(name);
    }
}
