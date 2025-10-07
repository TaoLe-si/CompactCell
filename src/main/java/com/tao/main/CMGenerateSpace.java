package com.tao.main;

import com.tao.main.blockentity.CMInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CMGenerateSpace {
    private static CMCellTiers tier;
    private static int radius = 0;
    private static UUID mHolder;
    public class CubeGenerator {

        public static class CubeFaces {
            public List<BlockPos> topFace = new ArrayList<>();
            public List<BlockPos> bottomFace = new ArrayList<>();
            public List<BlockPos> northFace = new ArrayList<>();
            public List<BlockPos> southFace = new ArrayList<>();
            public List<BlockPos> eastFace = new ArrayList<>();
            public List<BlockPos> westFace = new ArrayList<>();

            public BlockPos topCenter;
            public BlockPos bottomCenter;
            public BlockPos northCenter;
            public BlockPos southCenter;
            public BlockPos eastCenter;
            public BlockPos westCenter;
        }

        /**
         * 生成立方体并分离各个面，同时计算每个面的中心点
         * @param level 世界
         * @param centerPos 立方体中心位置
         * @param radius 半径（从中心到边的距离）
         * @return 包含各个面和中心点的CubeFaces对象
         */
        public static CubeFaces generateCubeWithSeparatedFaces(Level level, BlockPos centerPos, int radius) {
            CubeFaces faces = new CubeFaces();
            int minX = centerPos.getX() - radius;
            int maxX = centerPos.getX() + radius;
            int minY = centerPos.getY() - radius;
            int maxY = centerPos.getY() + radius;
            int minZ = centerPos.getZ() - radius;
            int maxZ = centerPos.getZ() + radius;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (!level.isInWorldBounds(pos)) continue;
                        boolean isTop = (y == maxY);
                        boolean isBottom = (y == minY);
                        boolean isNorth = (z == minZ);
                        boolean isSouth = (z == maxZ);
                        boolean isWest = (x == minX);
                        boolean isEast = (x == maxX);
                        boolean isOnSurface = isTop || isBottom || isNorth || isSouth || isWest || isEast;
                        if (isOnSurface) {
                            //level.setBlock(pos, blockState, 3);
                            if (isTop) {
                                faces.topFace.add(pos);
                                level.setBlock(pos, BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("compactcell", "smooth_stone_unbreakable")).defaultBlockState(), 3);
                                continue;
                            }
                            if (isBottom) {
                                faces.bottomFace.add(pos);
                                level.setBlock(pos, BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("compactcell", "smooth_stone_unbreakable")).defaultBlockState(), 3);
                                continue;
                            }
                            if (isNorth) faces.northFace.add(pos);
                            if (isSouth) faces.southFace.add(pos);
                            if (isWest) faces.westFace.add(pos);
                            if (isEast) faces.eastFace.add(pos);
                            level.setBlock(pos, BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("compactcell", "concrete_white_unbreakable")).defaultBlockState(), 3);
                        }
                    }
                }
            }
            calculateFaceCenters(faces);
            return faces;
        }
        private static void calculateFaceCenters(CubeFaces faces) {
            if (!faces.topFace.isEmpty()) {
                int sumX = 0, sumZ = 0;
                int y = faces.topFace.get(0).getY(); // 所有点的Y坐标相同
                for (BlockPos pos : faces.topFace) {
                    sumX += pos.getX();
                    sumZ += pos.getZ();
                }
                faces.topCenter = new BlockPos(sumX / faces.topFace.size(), y, sumZ / faces.topFace.size());
            }
            if (!faces.bottomFace.isEmpty()) {
                int sumX = 0, sumZ = 0;
                int y = faces.bottomFace.get(0).getY();
                for (BlockPos pos : faces.bottomFace) {
                    sumX += pos.getX();
                    sumZ += pos.getZ();
                }
                faces.bottomCenter = new BlockPos(sumX / faces.bottomFace.size(), y, sumZ / faces.bottomFace.size());
            }
            if (!faces.northFace.isEmpty()) {
                int sumX = 0, sumY = 0;
                int z = faces.northFace.get(0).getZ();
                for (BlockPos pos : faces.northFace) {
                    sumX += pos.getX();
                    sumY += pos.getY();
                }
                faces.northCenter = new BlockPos(sumX / faces.northFace.size(), sumY / faces.northFace.size(), z);
            }
            if (!faces.southFace.isEmpty()) {
                int sumX = 0, sumY = 0;
                int z = faces.southFace.get(0).getZ();

                for (BlockPos pos : faces.southFace) {
                    sumX += pos.getX();
                    sumY += pos.getY();
                }
                faces.southCenter = new BlockPos(sumX / faces.southFace.size(), sumY / faces.southFace.size(), z);
            }

            if (!faces.westFace.isEmpty()) {
                int sumY = 0, sumZ = 0;
                int x = faces.westFace.get(0).getX();

                for (BlockPos pos : faces.westFace) {
                    sumY += pos.getY();
                    sumZ += pos.getZ();
                }
                faces.westCenter = new BlockPos(x, sumY / faces.westFace.size(), sumZ / faces.westFace.size());
            }
            if (!faces.eastFace.isEmpty()) {
                int sumY = 0, sumZ = 0;
                int x = faces.eastFace.get(0).getX();

                for (BlockPos pos : faces.eastFace) {
                    sumY += pos.getY();
                    sumZ += pos.getZ();
                }
                faces.eastCenter = new BlockPos(x, sumY / faces.eastFace.size(), sumZ / faces.eastFace.size());
            }
        }

    }

    public static void setHolder(UUID holder) {
        mHolder = holder;
    }

    public static UUID getHolder() {
        return mHolder;
    }

    public static void setTier(CMCellTiers tiers) {
        tier = tiers;
    }

    public static void generate(ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        Level level = player.level();
        radius = 6;
        radius = radius + tier.ordinal() * tier.ordinal() * 2;
        BlockPos platformCenter = new BlockPos(playerPos.getX(), playerPos.getY() + radius - 1, playerPos.getZ());
        CubeGenerator.CubeFaces faces = CubeGenerator.generateCubeWithSeparatedFaces(player.level(), platformCenter, radius);
        level.setBlock(faces.bottomCenter, BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("compactcell", "interface")).defaultBlockState(), 3);
        CMInterfaceBlockEntity blockEntity = (CMInterfaceBlockEntity) level.getBlockEntity(faces.bottomCenter);
        if (blockEntity != null) {
            blockEntity.setHolder(mHolder);
            CompactCell.LOG.info(String.valueOf(mHolder));
        }
    }
}
