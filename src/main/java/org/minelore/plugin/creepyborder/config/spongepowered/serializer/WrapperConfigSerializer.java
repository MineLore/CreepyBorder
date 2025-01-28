package org.minelore.plugin.creepyborder.config.spongepowered.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.minelore.plugin.creepyborder.config.WrapperConfig;
import org.minelore.plugin.creepyborder.util.NameClassContainer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.CoercionFailedException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class WrapperConfigSerializer implements TypeSerializer<WrapperConfig> {
    private static final String FIELD_NAME = "name";

    private final Map<String, Class<? extends WrapperConfig>> nameClassContainer = new HashMap<>();
    private final Map<String, ObjectMapper<WrapperConfig>> objectMapperMap = new HashMap<>();

    public WrapperConfigSerializer(Map<String, Class<? extends WrapperConfig>> nameConfigs) {
        for (Map.Entry<String, Class<? extends WrapperConfig>> stringClassEntry : nameConfigs.entrySet()) {
            try {
                addWrapperConfig(stringClassEntry.getValue(), stringClassEntry.getKey());
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T extends WrapperConfig> void addWrapperConfig(Class<T> clazz, String name) throws SerializationException {
        nameClassContainer.put(name, clazz);
        objectMapperMap.put(name, (ObjectMapper<WrapperConfig>) ObjectMapper.factory().get(clazz));
    }

    @Override
    public WrapperConfig deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String name = node.node(FIELD_NAME).getString();
        if (name == null) {
            new SerializationException("Could not deserialize wrapped config because name is null");
        }
        Class<? extends WrapperConfig> clazz = nameClassContainer.get(name);
        if (clazz == null) {
            throw new SerializationException("Could not deserialize wrapped config because class for name '" + name + "' is not found in registered wrapped configs");
        }
        return objectMapperMap.get(name).load(node);
    }

    @Override
    public void serialize(Type type, @Nullable WrapperConfig obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Could not serialize wrapped config because it is null");
        }
        Class<? extends WrapperConfig> clazz = nameClassContainer.get(obj.getName());
        if (clazz == null) {
            throw new SerializationException("Could not serialize wrapped config because class for name '" + obj.getName() + "' is not found in registered wrapped configs");
        }
        if (!clazz.isInstance(obj)) {
            throw new SerializationException("Could not serialize wrapped config because class of obj does not match the registered class for name '" + obj.getName() + "'");
        }
        ObjectMapper<WrapperConfig> objectMapper = (ObjectMapper<WrapperConfig>) ObjectMapper.factory().get(clazz);
        objectMapper.save(obj, node);
    }
}
