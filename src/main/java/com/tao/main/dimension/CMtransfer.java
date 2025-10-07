package com.tao.main.dimension;

import appeng.api.stacks.AEItemKey;
import com.tao.main.CMCellTiers;
import com.tao.main.CMSavedData;
import com.tao.main.CompactCell;
import com.tao.main.events.CMPlayerChangedDimensionEvent;
import com.tao.main.items.CMBaseCellItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

import static com.tao.main.dimension.CMCompactDimension.WORLD_ID;
import static com.tao.main.dimension.CMCompactDimension.forServer;

public class CMtransfer extends CMSavedData {
    private static int count = 0;
    private static final int r = 1000;
    private static final HashMap<Integer, CMCellTiers> idMap = new HashMap<>();

    public static void trans(AEItemKey clickedItem, ServerPlayer player, int pid) {
        ResourceKey<Level> dest;
        Vec3 pos = new Vec3(0, 0, 0);
        Vec2 rotation;
        CMHistory history = CMHistory.getInstance(player, pid);
        CMCellTiers tiers = idMap.get(pid);
        if (tiers == null) {
            idMap.put(pid, ((CMBaseCellItem) clickedItem.getItem()).getTier());
            tiers = idMap.get(pid);
        }

        if (history.getPid() != pid) return;
        if (player.level().dimension().equals(WORLD_ID)) {
            dest = history.getFormer();
            pos = history.getPosition();
            rotation = history.getRotation();
            CMHistory.remove(history);
        } else {
            dest = WORLD_ID;
            for (int i = 0; i <= count; i++) {
                if (i == pid) {
                    pos = new Vec3(i % 2 * r, 0, ((double) i / 2) * r);
                }
            }
            rotation = new Vec2(0, 0);
            CMPlayerChangedDimensionEvent.transfer(pid, tiers);
        }
        player.changeDimension(CMDimensionTransitions.to(forServer(player.server, dest), pos, rotation));

    }

    public static void add() {
        count++;
    }

    public static int getCount() {
        return count;
    }
    public static void clear() {
        if (idMap != null) idMap.clear();
        count = 0;
    }

    public static CMtransfer create() {
        var data = new CMtransfer();
        //CompactCell.LOG.info("============> 尝试获取 CMtransfer 对象\n\n\n");
        clear();
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("space_count", count);

        CompoundTag mapTag = new CompoundTag();
        for (Map.Entry<Integer, CMCellTiers> entry : idMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = entry.getValue().name();
            mapTag.putString(key, value);
        }
        tag.put("id_map", mapTag);

        //CompactCell.LOG.info("保存 CMtransfer 数据 - count: {}, map大小: {}", count, idMap.size());
        //CompactCell.LOG.info("保存的 HashMap: {}", idMap);

        return tag;
    }

    public static CMtransfer load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        var data = CMtransfer.create();
        clear();
        //CompactCell.LOG.info("开始加载 CMtransfer 数据\n\n\n");

        if (tag.contains("space_count")) {
            count = tag.getInt("space_count");
            //CompactCell.LOG.info("成功加载 count: {}", count);
        } else {
            //CompactCell.LOG.warn("未找到 space_count，使用默认值 0");
            count = 0;
        }


        if (tag.contains("id_map", CompoundTag.TAG_COMPOUND)) {
            CompoundTag mapTag = tag.getCompound("id_map");
            for (String key : mapTag.getAllKeys()) {
                try {
                    Integer pid = Integer.parseInt(key);
                    String valueStr = mapTag.getString(key);
                    CMCellTiers tier = CMCellTiers.valueOf(valueStr);
                    idMap.put(pid, tier);
                    //CompactCell.LOG.info("恢复映射: {} -> {}", pid, tier);
                } catch (NumberFormatException e) {
                    //CompactCell.LOG.error("无法解析键: {}", key);
                } catch (IllegalArgumentException e) {
                    //CompactCell.LOG.error("无效的 CMCellTiers 值: {}", mapTag.getString(key));
                }
            }
            //CompactCell.LOG.info("成功加载 HashMap，大小: {}", idMap.size());
        } else {
            //CompactCell.LOG.warn("未找到 id_map，使用空 HashMap");
        }
        //CompactCell.LOG.info("CMtransfer 数据加载完成");
        return data;
    }
}