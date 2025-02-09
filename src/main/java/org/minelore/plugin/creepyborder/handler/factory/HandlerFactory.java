package org.minelore.plugin.creepyborder.handler.factory;

import org.minelore.plugin.creepyborder.config.spongepowered.HandlerConfig;
import org.minelore.plugin.creepyborder.handler.Handler;

import java.util.function.Function;

/**
 * @author TheDiVaZo
 * created on 30.01.2025
 */
@FunctionalInterface
public interface HandlerFactory<T extends Handler, K extends HandlerConfig> extends Function<K, T> {
    T loadWrapper(K handlerConfig);

    @Override
    default T apply(K k) {
        return loadWrapper(k);
    }
}
