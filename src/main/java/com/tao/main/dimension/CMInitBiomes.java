package com.tao.main.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class CMInitBiomes {
    public static void initBiomes(BootstrapContext<Biome> context) {
        var spawnBuilder = new MobSpawnSettings.Builder();
        var spawns = spawnBuilder.build();

        final Biome compactBiome = new Biome.BiomeBuilder()
                .downfall(0)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .mobSpawnSettings(spawns)
                .hasPrecipitation(false)
                .temperature(0.8f)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(12638463)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0xFF000000)
                        .build())
                .build();

        context.register(ResourceKey.create(Registries.BIOME, CMCompactDimension.BIOME_ID), compactBiome);
    }
}
