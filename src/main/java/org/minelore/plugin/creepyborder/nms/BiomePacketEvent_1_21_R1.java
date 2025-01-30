package org.minelore.plugin.creepyborder.nms;

import com.comphenix.protocol.events.PacketEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.minelore.plugin.creepyborder.util.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class BiomePacketEvent_1_21_R1 implements BiomePacketEvent {
    private final BiomeWrapper_1_21_R1 biome;

    private static final Field chunkBiomesField = ReflectionUtil.getField(ClientboundLevelChunkPacketData.class, byte[].class);

    public BiomePacketEvent_1_21_R1(BiomeWrapper_1_21_R1 biome) {
        this.biome = biome;
    }

    public void replaceBiomeInChunkPacket(PacketEvent event) {
        Player player = event.getPlayer();
        Registry<Biome> biomes = MinecraftServer.getServer().registryAccess().registry(Registries.BIOME).orElseThrow();
        ClientboundLevelChunkWithLightPacket packet = (ClientboundLevelChunkWithLightPacket) event.getPacket().getHandle();
        ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
        int ySections = ((CraftWorld) player.getWorld()).getHandle().getSectionsCount();
        LevelChunkSection[] sections = new LevelChunkSection[ySections];

        FriendlyByteBuf sectionBuffer = chunkData.getReadBuffer();
        int bufferSize = 0;
        for (int i = 0; i < ySections; i++) {
            sections[i] = new LevelChunkSection(biomes, null, null, ySections - i);
            sections[i].read(sectionBuffer);

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    for (int z = 0; z < 4; z++) {
                        sections[i].setBiome(x, y, z, Holder.direct(biome.getBiome()));
                    }
                }
            }
            bufferSize += sections[i].getSerializedSize();
        }

        byte[] bytes = new byte[bufferSize];
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        buffer.writerIndex(0);
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(buffer);
        for (LevelChunkSection section : sections) {
            section.write(friendlyByteBuf);
        }
        ReflectionUtil.setField(chunkBiomesField, chunkData, bytes);

    }
}
