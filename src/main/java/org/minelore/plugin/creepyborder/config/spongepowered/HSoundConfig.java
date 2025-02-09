package org.minelore.plugin.creepyborder.config.spongepowered;

import org.bukkit.Sound;
import org.minelore.plugin.creepyborder.handler.SoundHandler;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

/**
 * @author TheDiVaZo
 * created on 28.01.2025
 */
@ConfigSerializable
public class HSoundConfig extends HandlerConfig {
    private List<Sound> sounds = List.of(
        Sound.AMBIENT_CAVE
    );
    private int pitch = 1;
    private int volume = 1;
    private int periodInTick = 20*15;  // 20 tick = 1 second

    public HSoundConfig() {
        super(SoundHandler.NAME);
    }

    public HSoundConfig(double distToBorder) {
        this();
        this.distToBorder = distToBorder;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public int getPitch() {
        return pitch;
    }

    public int getVolume() {
        return volume;
    }

    public int getPeriodInTick() {
        return periodInTick;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WSoundConfigImpl{");
        sb.append("sounds=").append(sounds);
        sb.append(", pitch=").append(pitch);
        sb.append(", volume=").append(volume);
        sb.append(", periodInTick=").append(periodInTick);
        sb.append(", name='").append(name).append('\'');
        sb.append(", distToBorder=").append(distToBorder);
        sb.append('}');
        return sb.toString();
    }
}
