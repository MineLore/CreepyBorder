package org.minelore.plugin.creepyborder.component;

import com.comphenix.protocol.events.PacketEvent;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public interface BiomePacketEvent {
    void replaceChunkPacketToRedWater(PacketEvent event);
}
