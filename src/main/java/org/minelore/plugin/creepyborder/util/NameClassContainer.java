package org.minelore.plugin.creepyborder.util;

import java.util.Collections;
import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class NameClassContainer {
    private final Map<String, Class<?>> classContainer;

    public NameClassContainer(Map<String, Class<?>> classContainer) {
        this.classContainer = Map.copyOf(classContainer);
    }

    public <T> Class<T> get(String name) {
        return (Class<T>) classContainer.get(name);
    }

    public boolean contains(String name) {
        return classContainer.containsKey(name);
    }

    public boolean contains(Class<?> clazz) {
        return classContainer.containsValue(clazz);
    }

    public Map<String, Class<?>> getContainer() {
        return classContainer;
    }
}
