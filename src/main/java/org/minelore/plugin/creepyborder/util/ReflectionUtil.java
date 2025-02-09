package org.minelore.plugin.creepyborder.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author TheDiVaZo
 * created on 27.01.2025
 * <p>
 * Скоммунизжено из com.cjcrafter:mechanicscore:3.4.1
 */
public class ReflectionUtil {

    public static void setField(@NotNull Field field, @Nullable Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new InternalError("Failed to set field " + String.valueOf(field) + ". This is probably caused by your minecraft server version. Contact a DEV for more help.", e);
        }
    }

    public static Field getField(@NotNull Class<?> target, Class<?> type) {
        return getField(target, type, 0, false);
    }

    public static Field getField(@NotNull Class<?> target, Class<?> type, int index) {
        return getField(target, type, index, false);
    }

    public static Field getField(@NotNull Class<?> target, Class<?> type, int index, boolean skipStatic) {
        for (Field field : target.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType()) && (!skipStatic || !Modifier.isStatic(field.getModifiers())) && index-- <= 0) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                return field;
            }
        }

        Class<?> superClass = target.getSuperclass();
        if (superClass != null) {
            return getField(superClass, type, index);
        } else {
            throw new IllegalArgumentException("Cannot find field with type " + String.valueOf(type));
        }
    }

    public static @NotNull Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return field;
        } catch (SecurityException | NoSuchFieldException e) {
            throw new InternalError("Failed to get field " + fieldName + ". This is probably caused by your minecraft server version. Contact a DEV for more help.", e);
        }
    }
}
