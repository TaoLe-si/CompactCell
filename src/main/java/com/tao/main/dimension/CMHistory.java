package com.tao.main.dimension;

import com.tao.main.CMSavedData;
import com.tao.main.CompactCell;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

public class CMHistory extends CMSavedData {
    private static ArrayList<CMHistory> mhistory = new ArrayList<>();
    private ResourceKey<Level> former;
    private UUID id;
    private int mpid;
    private Vec3 position;
    private Vec2 rotation;
    private CMHistory(ServerPlayer player, int pid) {
        id = UUID.randomUUID();
        former = player.level().dimension();
        position = new Vec3(player.getX(), player.getY(), player.getZ());
        rotation = player.getRotationVector();
        mhistory.add(this);
        mpid = pid;
    }
    private CMHistory(UUID _id, ResourceKey<Level> _former, Vec3 _position, Vec2 _rotation, int _pid) {
        id = _id;
        former = _former;
        position = _position;
        rotation = _rotation;
        mhistory.add(this);
        mpid = _pid;
    }
    public CMHistory() {}
    public ResourceKey<Level> getFormer() {
        return former;
    }
    public Vec3 getPosition() {
        return position;
    }
    public Vec2 getRotation() {
        return rotation;
    }
    public int getPid() {
        return mpid;
    }
    public UUID getId() {
        return id;
    }
    public static CMHistory getInstance(ServerPlayer player, int pid) {
        for (CMHistory history : mhistory) {
            if (history.mpid == pid) {
                return history;
            }
        }
        return new CMHistory(player, pid);
    }
    public static void remove(CMHistory _history) {
        mhistory.removeIf(history -> history.id == _history.id);
    }

    public static void clear() {
        if (mhistory != null) mhistory.clear();
    }
    public static CMHistory create() {
        var data = new CMHistory();
        //CompactCell.LOG.info("============> 尝试获取 CMHistory 对象\n\n\n");
        return data;
    }
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag historyList = new ListTag();
        for (CMHistory history : mhistory) {
            CompoundTag historyTag = new CompoundTag();
            historyTag.putUUID("id", history.id);
            historyTag.putInt("mpid", history.mpid);
            if (history.former != null) {
                historyTag.putString("former", history.former.location().toString());
            }
            if (history.position != null) {
                CompoundTag posTag = new CompoundTag();
                posTag.putDouble("x", history.position.x);
                posTag.putDouble("y", history.position.y);
                posTag.putDouble("z", history.position.z);
                historyTag.put("position", posTag);
            }
            if (history.rotation != null) {
                CompoundTag rotTag = new CompoundTag();
                rotTag.putFloat("x", history.rotation.x);
                rotTag.putFloat("y", history.rotation.y);
                historyTag.put("rotation", rotTag);
            }
            historyList.add(historyTag);
        }
        tag.put("cm_history", historyList);
        //CompactCell.LOG.info("==================> 已保存 " + tag);
        return tag;
    }
    public static CMHistory load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        clear();
        //CompactCell.LOG.info("===========开始加载 CMHistory 数据\n\n\n");
        if (tag.contains("cm_history", Tag.TAG_LIST)) {
            ListTag historyList = tag.getList("cm_history", Tag.TAG_COMPOUND);
            for (int i = 0; i < historyList.size(); i++) {
                CompoundTag historyTag = historyList.getCompound(i);
                UUID id = historyTag.getUUID("id");
                int mpid = historyTag.getInt("mpid");
                ResourceKey<Level> former = null;
                if (historyTag.contains("former", Tag.TAG_STRING)) {
                    String formerStr = historyTag.getString("former");
                    ResourceLocation location = ResourceLocation.parse(formerStr);
                    former = ResourceKey.create(Registries.DIMENSION, location);
                }
                Vec3 position = null;
                if (historyTag.contains("position", Tag.TAG_COMPOUND)) {
                    CompoundTag posTag = historyTag.getCompound("position");
                    double x = posTag.getDouble("x");
                    double y = posTag.getDouble("y");
                    double z = posTag.getDouble("z");
                    position = new Vec3(x, y, z);
                }
                Vec2 rotation = null;
                if (historyTag.contains("rotation", Tag.TAG_COMPOUND)) {
                    CompoundTag rotTag = historyTag.getCompound("rotation");
                    float rotX = rotTag.getFloat("x");
                    float rotY = rotTag.getFloat("y");
                    rotation = new Vec2(rotX, rotY);
                }
                new CMHistory(id, former, position, rotation, mpid);
            }
        }
        //CompactCell.LOG.info("==================> 已加载 " + tag);
        return CMHistory.create();
    }
}
