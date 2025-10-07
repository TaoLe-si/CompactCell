package com.tao.main;

import appeng.api.networking.IGridNode;
import appeng.blockentity.storage.DriveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static appeng.api.networking.GridHelper.createConnection;
import static com.tao.main.dimension.CMCompactDimension.forServer;

public class CMConnectionsHelper extends CMSavedData {

    public CMConnectionsHelper() {};
    public static final Map<UUID, String> driveSpaceCellMap = new HashMap<>();
    public static final Map<UUID, Boolean> connectedDriveCellMap = new HashMap<>();

    // 生成唯一标识符的键名
    public static String getDriveKey(DriveBlockEntity drive) {
        CompactCell.LOG.info(drive.getBlockPos() + "_" + drive.getLevel().dimension().location());
        return drive.getBlockPos().toShortString() + "_" + drive.getLevel().dimension().location();
    }

    public static BlockPos getDrivePos(String driveKey) {
        String[] split = driveKey.split("_")[0].split(", ");
        return new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
    public static ResourceKey<Level> getDriveLevel(String driveKey) {
        String[] split = driveKey.split("_");
        ResourceLocation location = ResourceLocation.parse(split[1]);
        return ResourceKey.create(Registries.DIMENSION, location);
    }

    public static void tryConnet(UUID inter, Level _level, IGridNode iGridNode) {
        CompactCell.LOG.info("所有键值对: {}", driveSpaceCellMap);
        for (var entry : driveSpaceCellMap.entrySet()) {
            CompactCell.LOG.info("正在匹配: {}, {}", entry.getKey(), inter);
            if (entry.getKey() != null && entry.getKey().equals(inter)) {
                CompactCell.LOG.info("找到磁盘: {}, 驱动器: {}", entry.getKey(), entry.getValue());
                var pos = getDrivePos(entry.getValue());
                var server = _level.getServer();
                if (server != null) {
                    var level = forServer(server, getDriveLevel(entry.getValue()));
                    var blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof DriveBlockEntity drive) {
                        if (connectedDriveCellMap.get(inter) == null) {
                            CompactCell.LOG.info("建立连接");
                            createConnection(drive.getGridNode(), iGridNode);
                            connectedDriveCellMap.put(inter, true);
                        } else {
                            CompactCell.LOG.info("已建立过连接");
                        }
                    } else {
                        driveSpaceCellMap.remove(entry.getKey(), entry.getValue());
                        connectedDriveCellMap.remove(inter, true);
                    }
                }
            } else if (entry.getKey() == null) {
                driveSpaceCellMap.remove(entry.getKey(), entry.getValue());
                connectedDriveCellMap.remove(inter, true);
            }
        }
    }
    public static void clear() {
        driveSpaceCellMap.clear();
        connectedDriveCellMap.clear();
    }
    public static CMConnectionsHelper create() {
        var data = new CMConnectionsHelper();
        //CompactCell.LOG.info("============> 尝试获取 CMConnectionsHelper 对象\n\n\n");
        clear();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        //CompactCell.LOG.info("保存 CMConnectionsHelper 数据，共有 {} 个映射", driveSpaceCellMap.size());

        // 保存 driveSpaceCellMap
        ListTag mappingsList = new ListTag();
        for (Map.Entry<UUID, String> entry : driveSpaceCellMap.entrySet()) {
            CompoundTag mappingTag = new CompoundTag();
            if (entry.getKey() != null) {
                mappingTag.putUUID("uuid", entry.getKey());
                mappingTag.putString("driveKey", entry.getValue());
            }
            mappingsList.add(mappingTag);
        }
        tag.put("mappings", mappingsList);

        // 保存 connectedDriveCellMap
        ListTag connectedList = new ListTag();
        for (Map.Entry<UUID, Boolean> entry : connectedDriveCellMap.entrySet()) {
            CompoundTag connectedTag = new CompoundTag();
            if (entry.getKey() != null) {
                connectedTag.putUUID("uuid", entry.getKey());
                connectedTag.putBoolean("connected", entry.getValue());
            }
            connectedList.add(connectedTag);
        }
        tag.put("connectedMappings", connectedList);

        //CompactCell.LOG.info("CMConnectionsHelper 数据保存完成");
        return tag;
    }

    public static CMConnectionsHelper load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        CMConnectionsHelper data = create();
        clear();
        if (tag.contains("mappings", Tag.TAG_LIST)) {
            ListTag mappingsList = tag.getList("mappings", Tag.TAG_COMPOUND);
            //CompactCell.LOG.info("开始加载 CMConnectionsHelper 数据，共有 {} 个映射", mappingsList.size());
            for (int i = 0; i < mappingsList.size(); i++) {
                CompoundTag mappingTag = mappingsList.getCompound(i);
                try {
                    UUID uuid = mappingTag.getUUID("uuid");
                    String driveKey = mappingTag.getString("driveKey");
                    driveSpaceCellMap.put(uuid, driveKey);
                    //CompactCell.LOG.debug("加载映射: {} -> {}", uuid, driveKey);
                } catch (Exception e) {
                    //CompactCell.LOG.error("加载映射数据时出错: {}", e.getMessage());
                }
            }
        }
        return data;
    }
}
