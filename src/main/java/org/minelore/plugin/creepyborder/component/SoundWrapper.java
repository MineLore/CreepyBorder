package org.minelore.plugin.creepyborder.component;

import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class SoundWrapper extends BukkitTaskWrapper {
    private final Random random = ThreadLocalRandom.current();
    private static final String NAME = "Sound";


    private final List<Sound> sounds;
    private final int pitch;
    private final int volume;
    private final int periodInTick;

    public SoundWrapper(CreepyBorder plugin, List<Sound> sounds, int pitch, int volume, int periodInTick) {
        super(plugin, NAME);
        this.sounds = List.copyOf(sounds);
        this.pitch = pitch;
        this.volume = volume;
        this.periodInTick = periodInTick;
    }


    @Override
    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            org.bukkit.Sound sound = sounds.get(random.nextInt(sounds.size()));
            for (Player player : activePlayer) {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }
        }, 0, periodInTick);
    }
}
