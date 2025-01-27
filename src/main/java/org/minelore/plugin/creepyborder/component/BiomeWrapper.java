package org.minelore.plugin.creepyborder.component;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.minelore.plugin.creepyborder.CreepyBorder;

import static com.comphenix.protocol.PacketType.Play.Server.MAP_CHUNK;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class BiomeWrapper extends AbstractWrapper {
    private static final String NAME = "Biome";

    private final PacketListener packetListener;
    private final ProtocolManager protocolManager;

    public BiomeWrapper(CreepyBorder plugin, BiomePacketEvent biomePacketEvent, ProtocolManager protocolManager) {
        super(plugin, NAME);
        this.packetListener = new PacketAdapter(plugin, MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (activePlayer.contains(event.getPlayer())) {
                    biomePacketEvent.handleChunkBiomesPacket(event);
                }
            }
        };
        this.protocolManager = protocolManager;
    }

    @Override
    public void start() {
        protocolManager.addPacketListener(packetListener);
    }

    @Override
    public void stop() {
        protocolManager.removePacketListener(packetListener);
    }
}
