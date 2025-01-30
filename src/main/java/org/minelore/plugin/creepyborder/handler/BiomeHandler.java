package org.minelore.plugin.creepyborder.handler;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.minelore.plugin.creepyborder.CreepyBorder;
import org.minelore.plugin.creepyborder.nms.BiomePacketEvent;

import java.util.Optional;

import static com.comphenix.protocol.PacketType.Play.Server.MAP_CHUNK;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class BiomeHandler extends AbstractHandler {
    public static final String NAME = "Biome";

    private final PacketListener packetListener;
    private final ProtocolManager protocolManager;

    public BiomeHandler(CreepyBorder plugin, BiomePacketEvent biomePacketEvent, ProtocolManager protocolManager) {
        super(plugin, NAME);
        this.packetListener = new PacketAdapter(plugin, MAP_CHUNK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (activePlayer.contains(event.getPlayer()) && !event.getPlayer().isDead()) {
                    biomePacketEvent.replaceBiomeInChunkPacket(event);
                }
            }
        };
        this.protocolManager = protocolManager;
    }

    public void updateChunks(Player player) {
        Location location = player.getLocation();
        Optional<World> randomWorld = Bukkit.getWorlds().stream().filter(world -> world != player.getWorld()).findFirst();

        randomWorld.ifPresent(world -> player.teleport(world.getSpawnLocation()));
        player.teleport(location);
    }

    @Override
    public void interact(Player player) {
        if (!activePlayer.contains(player)) {
            super.interact(player);
            updateChunks(player);
        }
    }

    @Override
    public void cancel(Player player) {
        if (activePlayer.contains(player)) {
            super.cancel(player);
            updateChunks(player);
        }
    }

    @Override
    public void start() {
        protocolManager.addPacketListener(packetListener);
    }

    @Override
    public void stop() {
        protocolManager.removePacketListener(packetListener);
        for (Player player : activePlayer) {
            updateChunks(player);
        }
        activePlayer.clear();
    }
}
