package org.minelore.plugin.creepyborder.config;

import org.minelore.plugin.creepyborder.component.Wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class WrapperFactory {
    private final Map<String, Function<? extends WrapperConfig, Wrapper>> wrappersFactories = new HashMap<>();

    public WrapperFactory() {

    }

    public <T extends WrapperConfig> void registerWrapper(String name, Function<T, Wrapper> factory) {
        wrappersFactories.put(name, factory);
    }

    public <V extends Wrapper, T extends WrapperConfig> Optional<V> toWrapper(T config) {
        Function<T, V> function = (Function<T, V>) wrappersFactories.get(config.getName());
        if (function == null) return Optional.empty();
        return Optional.ofNullable(function.apply(config));
    }
}
