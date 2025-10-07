package com.tao.main;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerVisitData extends SavedData {
    private final Set<UUID> visitedPlayers = new HashSet<>();
    public static PlayerVisitData create() {
        return new PlayerVisitData();
    }
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag visitsTag = new CompoundTag();
        for (UUID playerId : visitedPlayers) {
            visitsTag.putBoolean(playerId.toString(), true);
        }
        tag.put("visitedPlayers", visitsTag);
        //CompactCell.LOG.info("保存 visitedPlayers 数据\n");
        //CompactCell.LOG.info("保存的 HashMap: {}", visitedPlayers);
        return tag;
    }

    public static PlayerVisitData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        //CompactCell.LOG.info("开始加载 PlayerVisitData 数据");
        PlayerVisitData data = PlayerVisitData.create();
        CompoundTag visitsTag = tag.getCompound("visitedPlayers");
        for (String key : visitsTag.getAllKeys()) {
            UUID playerId = UUID.fromString(key);
            data.visitedPlayers.add(playerId);
        }
        //CompactCell.LOG.info("PlayerVisitData 数据加载完成");
        return data;
    }

    public boolean isFirstVisit(UUID playerId) {
        return !visitedPlayers.contains(playerId);
    }

    public void markPlayerVisited(UUID playerId) {
        visitedPlayers.add(playerId);
        setDirty();
    }
}