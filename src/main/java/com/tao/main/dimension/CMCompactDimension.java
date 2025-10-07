package com.tao.main.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

public class CMCompactDimension {
    public static final ResourceKey<DimensionType> DIM_ID = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath("compactcell", "space_item_cell_world"));
    public static final ResourceKey<Level> WORLD_ID = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("compactcell", "space_item_cell_world"));
    public static ResourceLocation DIM_TYPE_KEY_SKY_PROPERTIES_ID = ResourceLocation.fromNamespaceAndPath("compactcell", "space_item_cell_world");
    public static final ResourceLocation BIOME_ID = ResourceLocation.fromNamespaceAndPath("compactcell", "space_item_cell_world");
    @NotNull
    public static ServerLevel forServer(MinecraftServer server, ResourceKey<Level> level) {
        return server.getLevel(level);
    }
}
