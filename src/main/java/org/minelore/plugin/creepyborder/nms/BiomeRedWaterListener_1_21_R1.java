package org.minelore.plugin.creepyborder.nms;

import com.comphenix.protocol.events.PacketEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.minelore.plugin.creepyborder.component.BiomePacketEvent;
import org.minelore.plugin.creepyborder.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class BiomeRedWaterListener_1_21_R1 implements BiomePacketEvent {
    private final Registry<Biome> registryBiome;
    private final Biome biome;

    private static final Field chunkBiomesField = ReflectionUtil.getField(ClientboundLevelChunkPacketData.class, byte[].class);

    public BiomeRedWaterListener_1_21_R1() {
        registryBiome = MinecraftServer.getServer().registryAccess().registry(Registries.BIOME).orElseThrow();
        biome = new Biome.BiomeBuilder()
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0xFF0000)
                        .skyColor(0xFF0000)
                        .fogColor(0xFF0000)
                        .waterFogColor(0xFF0000)
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST)
                        .build())
                .temperature(10)
                .downfall(10)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }

    public void handleChunkBiomesPacket(PacketEvent event) {
        Player player = event.getPlayer();
        ClientboundLevelChunkWithLightPacket packet = (ClientboundLevelChunkWithLightPacket) event.getPacket().getHandle();
        LevelChunk levelChunk = ((CraftPlayer) player).getHandle().level().getChunk(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockZ()
        );
        ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
        levelChunk.getSections();
        int ySections = ((CraftWorld) player.getWorld()).getHandle().getSectionsCount();
        LevelChunkSection[] sections = new LevelChunkSection[ySections];

        FriendlyByteBuf sectionBuffer = chunkData.getReadBuffer();
        int bufferSize = 0;
        for (int i = 0; i < ySections; i++) {
            sections[i] = new LevelChunkSection(registryBiome, null, null, ySections-i);
            sections[i].read(sectionBuffer);

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    for (int z = 0; z < 4; z++) {
                        sections[i].setBiome(x, y, z, Holder.direct(biome));
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
