package org.minelore.plugin.creepyborder.nms;

import com.comphenix.protocol.events.PacketEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.minelore.plugin.creepyborder.component.BiomePacketEvent;
import org.minelore.plugin.creepyborder.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author TheDiVaZo
 * created on 26.01.2025
 */
public class BiomeRedWaterListener_1_21_R1 implements BiomePacketEvent {
    private final Registry<Biome> registryBiome;
    private final Biome biome;
    private final NamespacedKey key;

    private final Color waterColor;
    private final Color skyColor;
    private final Color fogColor;
    private final Color waterFogColor;

    private static final Field chunkBiomesField = ReflectionUtil.getField(ClientboundLevelChunkPacketData.class, byte[].class);

    public BiomeRedWaterListener_1_21_R1(Color waterColor, Color skyColor, Color fogColor, Color waterFogColor) {
        this.waterColor = waterColor;
        this.skyColor = skyColor;
        this.fogColor = fogColor;
        this.waterFogColor = waterFogColor;
        registryBiome = MinecraftServer.getServer().registryAccess().registry(Registries.BIOME).orElseThrow();
        key = new NamespacedKey(("creepyborder").toLowerCase(Locale.ROOT), ("redwater").toLowerCase(Locale.ROOT));
        biome = new Biome.BiomeBuilder()
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(Color.RED.asRGB())
                        .skyColor(Color.RED.asRGB())
                        .fogColor(Color.RED.asRGB())
                        .waterFogColor(Color.RED.asRGB())
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST)
                        .build())
                .temperature(10)
                .downfall(10)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
        registerBiome();
    }

    private void registerBiome() {
        Registry<Biome> biomes = MinecraftServer.getServer().registryAccess().registry(Registries.BIOME).orElseThrow();
        ResourceKey<Biome> resource = ResourceKey.create(biomes.key(), ResourceLocation.fromNamespaceAndPath(key.getNamespace(), key.getKey()));

        // In order to add biomes, we need to use a writable registry.
        if (!(biomes instanceof WritableRegistry<Biome> writable))
            throw new InternalError(biomes + " was not a writable registry???");

        // Register the biome to BiomeManager's registry, and to the vanilla registry
        Field freezeField = ReflectionUtil.getField(MappedRegistry.class, boolean.class);
        ReflectionUtil.setField(freezeField, biomes, false);

        Field intrusiveHoldersField = ReflectionUtil.getField(MappedRegistry.class, "m");
        ReflectionUtil.setField(intrusiveHoldersField, biomes, new HashMap<>());

        writable.createIntrusiveHolder(biome);
        writable.register(resource, biome, RegistrationInfo.BUILT_IN);

        ReflectionUtil.setField(intrusiveHoldersField, biomes, null);
        ReflectionUtil.setField(freezeField, biomes, true);
    }

    public void replaceChunkPacketToRedWater(PacketEvent event) {
        Player player = event.getPlayer();
        ClientboundLevelChunkWithLightPacket packet = (ClientboundLevelChunkWithLightPacket) event.getPacket().getHandle();
        ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
        int ySections = ((CraftWorld) player.getWorld()).getHandle().getSectionsCount();
        LevelChunkSection[] sections = new LevelChunkSection[ySections];

        FriendlyByteBuf sectionBuffer = chunkData.getReadBuffer();
        int bufferSize = 0;
        for (int i = 0; i < ySections; i++) {
            sections[i] = new LevelChunkSection(registryBiome, null, null, ySections - i);
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
