package com.tao.main.events;

import appeng.api.storage.cells.CellState;
import appeng.blockentity.storage.DriveBlockEntity;
import com.tao.main.CMDataComponents;
import com.tao.main.CMItems;
import com.tao.main.CMTags;
import com.tao.main.CompactCell;
import com.tao.main.blockentity.CMInterfaceBlockEntity;
import com.tao.main.items.CMPortableSpaceCellItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.*;

import static com.tao.main.CMConnectionsHelper.*;
import static com.tao.main.dimension.CMCompactDimension.forServer;

public class CMPlayerUseBlockEvent {
    @SubscribeEvent
    public static void onPlayerUseBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        var level = event.getLevel();
        var blockEntity = level.getBlockEntity(event.getHitVec().getBlockPos());
        var item = event.getItemStack();
        if (blockEntity instanceof CMInterfaceBlockEntity && item.is(CMTags.WRENCH)) {
            event.setCanceled(true);
        }
        if (item.getItem() == CMItems.BONDER_ITEM.get()) {
            event.setCanceled(true);
            if (blockEntity instanceof DriveBlockEntity drive) {
                String driveKey = getDriveKey(drive);
                for (int i = 0; i < drive.getCellCount(); i++) {
                    Item cellItem = drive.getCellItem(i);
                    if (cellItem instanceof CMPortableSpaceCellItem) {
                        ItemStack stack = drive.getInternalInventory().getStackInSlot(i);
                        UUID uuid = stack.get(CMDataComponents.Id);
                        driveSpaceCellMap.put(uuid, driveKey);
                        //CompactCell.LOG.info("正在存储uuid: {}, driveKey: {}", uuid, driveKey);
                    }
                }
            }
            for (var entry : driveSpaceCellMap.entrySet()) {
                var drive = entry.getKey();
                var driveKey = entry.getValue();
                var pos = getDrivePos(driveKey);
                var server = level.getServer();
                if (server != null) {
                    var plevel = forServer(server, getDriveLevel(driveKey));
                    if (!(plevel.getBlockEntity(pos) instanceof DriveBlockEntity)) driveSpaceCellMap.remove(drive, driveKey);
                }
            }
        }
    }
}
