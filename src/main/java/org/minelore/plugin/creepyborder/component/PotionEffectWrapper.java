package org.minelore.plugin.creepyborder.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

import java.util.List;
import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class PotionEffectWrapper extends BukkitTaskWrapper {

    private static final String NAME = "PotionEffect";

    private static final int PERIOD_TASK_IN_SECOND = 5;
    private static final int DURATION_EFFECT_IN_SECOND = 7;

    public record EffectData(PotionEffectType type, int amplifier) {}

    private final List<PotionEffect> potionEffects;

    public PotionEffectWrapper(CreepyBorder plugin, Set<EffectData> effectDatas) {
        super(plugin, NAME);
        this.potionEffects = effectDatas.stream().map(this::getPotionEffect).toList();
    }

    @Override
    protected BukkitTask runTask() {
        return Bukkit.getScheduler().runTaskTimer(plugin, ()->{
            for (Player player : activePlayer) {
                player.addPotionEffects(potionEffects);
            }
        }, 0, 20L*PERIOD_TASK_IN_SECOND);
    }

    protected PotionEffect getPotionEffect(EffectData effectData) {
        return new PotionEffect(effectData.type(), 20*DURATION_EFFECT_IN_SECOND, effectData.amplifier());
    }

}
