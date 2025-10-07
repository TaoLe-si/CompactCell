package com.tao.main.events;

import appeng.api.networking.IGridNode;
import com.tao.main.CMCellTiers;
import com.tao.main.CMGenerateSpace;
import com.tao.main.CMSavedData;
import com.tao.main.CompactCell;
import com.tao.main.dimension.CMCompactDimension;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;

public class CMPlayerChangedDimensionEvent extends CMSavedData {
    private static HashMap<Integer, Boolean> idMap = new HashMap<>();
    private static int pid = 0;
    private static ItemStack mHolder;
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getTo().equals(CMCompactDimension.WORLD_ID)) {
            return;
        }
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (isFirstTimeEntering()) {
            //CompactCell.LOG.info("玩家初次进入该原件空间");
            markPlayerAsEntered();
            CMGenerateSpace.generate(player);
        }
    }

    private static boolean isFirstTimeEntering() {
        return idMap.get(pid) == null;
    }

    private static void markPlayerAsEntered() {
        idMap.put(pid, true);
    }

    public static void transfer(int id, CMCellTiers tiers) {
        pid = id;
        CMGenerateSpace.setTier(tiers);
    }
    public static void clear() {
        if (idMap != null) idMap.clear();
    }
    public static CMPlayerChangedDimensionEvent create() {
        var data = new CMPlayerChangedDimensionEvent();
        //CompactCell.LOG.info("============> 尝试获取 CMPlayerChangedDimension 对象\n\n\n");
        clear();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        var mapTag = new CompoundTag();
        for (Map.Entry<Integer, Boolean> entry : idMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Boolean value = entry.getValue();
            mapTag.putBoolean(key, value);
        }
        tag.put("id_map", mapTag);

        //CompactCell.LOG.info("保存 CMPlayerChangedDimension 数据 - map大小: {}", idMap.size());
        //CompactCell.LOG.info("保存的 HashMap: {}", idMap);

        return tag;
    }

    public static CMPlayerChangedDimensionEvent load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        var data = CMPlayerChangedDimensionEvent.create();
        clear();
        //CompactCell.LOG.info("===========开始加载 CMPlayerChangedDimension 数据\n\n\n");
        if (tag.contains("id_map", CompoundTag.TAG_COMPOUND)) {
            CompoundTag mapTag = tag.getCompound("id_map");
            for (String key : mapTag.getAllKeys()) {
                Integer pid = Integer.parseInt(key);
                Boolean valueStr = mapTag.getBoolean(key);
                idMap.put(pid, valueStr);
                //CompactCell.LOG.info("恢复映射: {} -> {}", pid, valueStr);
            }
            //CompactCell.LOG.info("成功加载 HashMap，大小: {}", idMap.size());
        } else {
            //CompactCell.LOG.warn("未找到 id_map，使用空 HashMap");
        }
        //CompactCell.LOG.info("\n\n\nCMPlayerChangedDimension 数据加载完成");
        return data;
    }
}
