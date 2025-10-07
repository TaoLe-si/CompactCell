package com.tao.main.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;


public final class CMInitDimensionTypes {
    private CMInitDimensionTypes() {
    }

    public static void initDimension(BootstrapContext<DimensionType> context) {
        DimensionType dimensionType = createSpatialDimensionType();
        context.register(CMCompactDimension.DIM_ID, dimensionType);
    }

    public static void initLevel(BootstrapContext<LevelStem> ctx) {
        final var biomes = ctx.lookup(Registries.BIOME);
        final var dimTypes = ctx.lookup(Registries.DIMENSION_TYPE);
        final var cmBiome = biomes.getOrThrow(ResourceKey.create(Registries.BIOME, CMCompactDimension.BIOME_ID));
        var flatSettings = new FlatLevelGeneratorSettings(Optional.empty(), cmBiome, Collections.emptyList())
                .withBiomeAndLayers(
                        List.of(new FlatLayerInfo(256, Blocks.AIR)),
                        Optional.empty(),
                        cmBiome
                );

        var stem = new LevelStem(dimTypes.getOrThrow(CMCompactDimension.DIM_ID), new FlatLevelSource(flatSettings));
        ctx.register(ResourceKey.create(Registries.LEVEL_STEM, CMCompactDimension.WORLD_ID.location()), stem);
    }

    private static DimensionType createSpatialDimensionType() {
        return new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                false, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                CMCompactDimension.DIM_TYPE_KEY_SKY_PROPERTIES_ID, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0));
    }
}
