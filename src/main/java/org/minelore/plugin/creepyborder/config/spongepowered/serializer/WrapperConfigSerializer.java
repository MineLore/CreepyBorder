package org.minelore.plugin.creepyborder.config.spongepowered.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.minelore.plugin.creepyborder.config.HandlerConfig;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class WrapperConfigSerializer implements TypeSerializer<HandlerConfig> {
    private static final String FIELD_NAME = "name";

    private final Map<String, Class<? extends HandlerConfig>> nameClassContainer = new HashMap<>();

    public WrapperConfigSerializer(Map<String, Class<? extends HandlerConfig>> nameConfigs) {
        for (Map.Entry<String, Class<? extends HandlerConfig>> stringClassEntry : nameConfigs.entrySet()) {
            try {
                addWrapperConfig(stringClassEntry.getValue(), stringClassEntry.getKey());
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public <T extends HandlerConfig> void addWrapperConfig(Class<T> clazz, String name) throws SerializationException {
        nameClassContainer.put(name, clazz);
    }

    @Override
    public HandlerConfig deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String name = node.node(FIELD_NAME).getString();
        if (name == null) {
            new SerializationException("Could not deserialize wrapped config because name is null");
        }
        Class<? extends HandlerConfig> clazz = nameClassContainer.get(name);
        if (clazz == null) {
            throw new SerializationException("Could not deserialize wrapped config because class for name '" + name + "' is not found in registered wrapped configs");
        }
        return ObjectMapper.factory().get(clazz).load(node);
    }

    @Override
    public void serialize(Type type, @Nullable HandlerConfig obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Could not serialize wrapped config because it is null");
        }
        Class<? extends HandlerConfig> clazz = nameClassContainer.get(obj.getName());
        if (clazz == null) {
            throw new SerializationException("Could not serialize wrapped config because class for name '" + obj.getName() + "' is not found in registered wrapped configs");
        }
        if (!clazz.isInstance(obj)) {
            throw new SerializationException("Could not serialize wrapped config because class of obj does not match the registered class for name '" + obj.getName() + "'");
        }
        ObjectMapper<HandlerConfig> objectMapper = (ObjectMapper<HandlerConfig>) ObjectMapper.factory().get(clazz);

        // set name field to first positions in config
        ConfigurationNode copiedNode = node.copy();
        objectMapper.save(obj, copiedNode);
        ConfigurationNode nameNode = copiedNode.node(FIELD_NAME).copy();
        copiedNode.removeChild(nameNode);
        node.node(FIELD_NAME).from(nameNode);
        node.mergeFrom(copiedNode);
    }
}
