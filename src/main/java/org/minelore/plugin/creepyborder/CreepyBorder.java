package org.minelore.plugin.creepyborder;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.minelore.plugin.creepyborder.component.*;
import org.minelore.plugin.creepyborder.manager.BorderManager;
import org.minelore.plugin.creepyborder.nms.BiomeRedWaterListener_1_21_R1;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static com.comphenix.protocol.PacketType.Play.Server.MAP_CHUNK;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    private final Logger LOG = getLogger();
    private ProtocolManager protocolManager;
    private BorderManager borderManager;

    @Override
    public void onDisable() {
        super.onDisable();
        LOG.info("CreepyBorder plugin is disabled! Development by TheDiVaZo. For MineLoreSMP");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        LOG.info("CreepyBorder plugin is enabled! Development by TheDiVaZo. For MineLoreSMP");
        protocolManager = ProtocolLibrary.getProtocolManager();
        loadBorderManager();
        borderManager.start();
    }

    public void loadBorderManager() {
        borderManager = new BorderManager(this, "world", Set.of(
                new BorderManager.DataOfEnableWrapper(100, new SoundWrapper(this, List.of(Sound.MUSIC_OVERWORLD_DRIPSTONE_CAVES), 1, 10, 20 * 30)),
                new BorderManager.DataOfEnableWrapper(50, new BiomeWrapper(this, new BiomeRedWaterListener_1_21_R1(), protocolManager)),
                new BorderManager.DataOfEnableWrapper(20, new MagmaGrabWrapper(this, 0.5)),
                new BorderManager.DataOfEnableWrapper(10, new MagmaGrabWrapper(this, 5)),
                new BorderManager.DataOfEnableWrapper(10, new PotionEffectWrapper(this, Set.of(
                        new PotionEffectWrapper.EffectData(org.bukkit.potion.PotionEffectType.BLINDNESS, 1),
                        new PotionEffectWrapper.EffectData(PotionEffectType.NAUSEA, 1),
                        new PotionEffectWrapper.EffectData(PotionEffectType.SLOWNESS, 4)
                ))),
                new BorderManager.DataOfEnableWrapper(10, new TimedKill(this, 20*5))
        ), player -> false);
    }
}
