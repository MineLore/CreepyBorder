package org.minelore.plugin.creepyborder.nms;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.bukkit.NamespacedKey;
import org.minelore.plugin.creepyborder.util.SpecialEffectsData;
import org.minelore.plugin.creepyborder.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author TheDiVaZo
 * created on 29.01.2025
 */
public class BiomeWrapper_1_21_R1 implements BiomeWrapper {
    private static final Field specialEffectsField = ReflectionUtil.getField(Biome.class, BiomeSpecialEffects.class);

    private Biome biome;
    private NamespacedKey key;

    public BiomeWrapper_1_21_R1() {
    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return key;
    }

    @Override
    public void replaceBiome(SpecialEffectsData specialEffectsData) {
        if (biome == null) {
            key = new NamespacedKey(("creepyborder").toLowerCase(Locale.ROOT), ("creepy_biome").toLowerCase(Locale.ROOT));
            biome = new Biome.BiomeBuilder()
                    .specialEffects(buildSpecialEffects(specialEffectsData))
                    .temperature(10)
                    .downfall(10)
                    .mobSpawnSettings(MobSpawnSettings.EMPTY)
                    .generationSettings(BiomeGenerationSettings.EMPTY)
                    .build();
            registerBiome();
        }
        else {
            BiomeSpecialEffects biomeSpecialEffects = buildSpecialEffects(specialEffectsData);
            ReflectionUtil.setField(specialEffectsField, biome, biomeSpecialEffects);
        }
    }

    private BiomeSpecialEffects buildSpecialEffects(SpecialEffectsData specialEffectsData) {
        return new BiomeSpecialEffects.Builder()
                .waterColor(specialEffectsData.waterColor().asRGB())
                .skyColor(specialEffectsData.skyColor().asRGB())
                .fogColor(specialEffectsData.fogColor().asRGB())
                .waterFogColor(specialEffectsData.waterFogColor().asRGB())
                .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST)
                .build();
    }

    @Override
    public void registerBiome() {
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

    @Override
    public Biome getBiome() {
        return biome;
    }
}
