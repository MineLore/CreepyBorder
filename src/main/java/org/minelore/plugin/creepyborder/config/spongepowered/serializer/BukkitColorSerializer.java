package org.minelore.plugin.creepyborder.config.spongepowered.serializer;

import com.google.common.base.Strings;
import io.leangen.geantyref.TypeToken;
import org.bukkit.Color;
import org.spongepowered.configurate.serialize.CoercionFailedException;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.HexFormat;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
public class BukkitColorSerializer extends ScalarSerializer<Color> {
    public BukkitColorSerializer() {
        super(Color.class);
    }

    @Override
    public Color deserialize(Type type, Object value) throws SerializationException {
        if (value instanceof Integer) {
            return Color.fromRGB((int) value);
        }

        final String potential = value.toString().toLowerCase(Locale.ROOT);

        try {
            return Color.fromRGB(Integer.decode(potential));
        } catch (Exception ignore) {
        }
        if (potential.equals("aqua")) return Color.AQUA;
        if (potential.equals("black")) return Color.BLACK;
        if (potential.equals("blue")) return Color.BLUE;
        if (potential.equals("light_blue")) return Color.fromBGR(0xADD8E6);
        if (potential.equals("fuchsia")) return Color.FUCHSIA;
        if (potential.equals("gray")) return Color.GRAY;
        if (potential.equals("green")) return Color.GREEN;
        if (potential.equals("lime")) return Color.LIME;
        if (potential.equals("orange")) return Color.ORANGE;
        if (potential.equals("purple")) return Color.PURPLE;
        if (potential.equals("red")) return Color.RED;
        if (potential.equals("white")) return Color.WHITE;
        if (potential.equals("yellow")) return Color.YELLOW;
        if (potential.equals("maroon")) return Color.MAROON;
        if (potential.equals("navy")) return Color.NAVY;
        if (potential.equals("silver")) return Color.SILVER;
        if (potential.equals("teal")) return Color.TEAL;
        if (potential.equals("olive")) return Color.OLIVE;
        if (potential.equals("brown")) return Color.fromBGR(0x964B00);
        throw new CoercionFailedException(type, value, "bukkit_color");


    }

    @Override
    protected Object serialize(Color item, Predicate<Class<?>> typeSupported) {
          return "#" + Strings.padEnd(Integer.toHexString(item.asRGB()), 6, '0');
    }
}
