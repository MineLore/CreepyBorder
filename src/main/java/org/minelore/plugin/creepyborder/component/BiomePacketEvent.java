package org.minelore.plugin.creepyborder.component;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public interface BiomePacketEvent {
    void handleChunkBiomesPacket(PacketEvent event);
}
