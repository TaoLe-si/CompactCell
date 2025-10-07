package com.tao.main;

import appeng.api.config.Actionable;
import appeng.api.implementations.menuobjects.IPortableTerminal;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.StorageHelper;
import appeng.helpers.InventoryAction;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.me.common.MEStorageMenu;
import appeng.util.Platform;
import com.tao.main.dimension.CMtransfer;
import com.tao.main.events.CMPlayerChangedDimensionEvent;
import com.tao.main.items.CMBaseCellItem;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.util.UUID;

public class CMMEStorageMenu extends MEStorageMenu {
    public static final MenuType<CMMEStorageMenu> SPACE_ITEM_CELL_TYPE = MenuTypeBuilder
            .<CMMEStorageMenu, IPortableTerminal>create(CMMEStorageMenu::new, IPortableTerminal.class)
            .build(ResourceLocation.fromNamespaceAndPath("compactcell", "space_item_cell"));
    public CMMEStorageMenu(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }
    @Override
    public Player getPlayer() {
        var carried = super.getPlayer().getMainHandItem();
        if (!carried.isEmpty() && !carried.has(CMDataComponents.Id)) {
            carried.set(CMDataComponents.Id, UUID.randomUUID());
            CompactCell.LOG.info("所持物品: {}", carried);
            CompactCell.LOG.info("设置uuid: {}", carried.get(CMDataComponents.Id));
        }
        return super.getPlayer();
    }

    @Override
    protected void handleNetworkInteraction(ServerPlayer player, @Nullable AEKey clickedKey, InventoryAction action) {
        if (!canInteractWithGrid()) {
            return;
        }
        // Handle interactions where the player wants to put something into the network
        if (clickedKey == null) {
            if (action == InventoryAction.SPLIT_OR_PLACE_SINGLE || action == InventoryAction.ROLL_DOWN) {
                putCarriedItemIntoNetwork(true);
            } else if (action == InventoryAction.PICKUP_OR_SET_DOWN) {
                putCarriedItemIntoNetwork(false);
            }
            return;
        }

        // Any of the remaining actions are for items only
        if (!(clickedKey instanceof AEItemKey clickedItem)) {
            return;
        }

        switch (action) {
            case SHIFT_CLICK -> moveOneStackToPlayer(clickedItem);
            case ROLL_DOWN, ROLL_UP, PICKUP_SINGLE, PICKUP_OR_SET_DOWN -> {

            }
            case SPLIT_OR_PLACE_SINGLE -> {
                if (clickedItem.getItem() instanceof CMBaseCellItem && !clickedItem.getReadOnlyStack().has(CMDataComponents.Pid)) {
                    clickedItem.getReadOnlyStack().set(CMDataComponents.Pid, new CMDataComponents.PidRecord(CMtransfer.getCount()));
                    CMGenerateSpace.setHolder(getActionSource().player().get().getMainHandItem().get(CMDataComponents.Id));
                    CMtransfer.trans(clickedItem, player, CMtransfer.getCount());
                    CMtransfer.add();
                } else if (clickedItem.getItem() instanceof CMBaseCellItem) {
                    CMtransfer.trans(clickedItem, player, clickedItem.getReadOnlyStack().get(CMDataComponents.Pid).id());
                }
            }
            case CREATIVE_DUPLICATE -> {
                if (player.getAbilities().instabuild) {
                    var is = clickedItem.toStack();
                    is.setCount(is.getMaxStackSize());
                    setCarried(is);
                }
            }
            case MOVE_REGION -> {
                final int playerInv = player.getInventory().items.size();
                for (int slotNum = 0; slotNum < playerInv; slotNum++) {
                    if (!moveOneStackToPlayer(clickedItem)) {
                        break;
                    }
                }
            }
            default -> CompactCell.LOG.info("");
        }
    }

    protected void putCarriedItemIntoNetwork(boolean singleItem) {
        var heldStack = getCarried();

        var what = AEItemKey.of(heldStack);
        if (what == null) {
            return;
        }

        if (!what.getReadOnlyStack().has(CMDataComponents.Id)) {
            what.getReadOnlyStack().set(CMDataComponents.Id, UUID.randomUUID());
        }

        var amount = heldStack.getCount();
        if (singleItem) {
            amount = 1;
        }

        var inserted = StorageHelper.poweredInsert(energySource, storage, what, amount, this.getActionSource());
        setCarried(Platform.getInsertionRemainder(heldStack, inserted));
    }

    @Override
    protected int transferStackToMenu(ItemStack input) {
        if (!canInteractWithGrid()) {
            return super.transferStackToMenu(input);
        }

        var key = AEItemKey.of(input);
        if (key == null || !isKeyVisible(key)) {
            return 0;
        }

        if (!key.getReadOnlyStack().has(CMDataComponents.Id)) {
            key.getReadOnlyStack().set(CMDataComponents.Id, UUID.randomUUID());
        }

        return (int) StorageHelper.poweredInsert(energySource, storage, key, input.getCount(), this.getActionSource());
    }

    private boolean moveOneStackToPlayer(AEItemKey what) {
        var potentialAmount = storage.extract(what, what.getMaxStackSize(), Actionable.SIMULATE, getActionSource());
        if (potentialAmount <= 0) {
            return false; // No item available
        }

        var destinationSlots = getQuickMoveDestinationSlots(what.toStack(), false);

        for (var destinationSlot : destinationSlots) {
            var amount = getPlaceableAmount(destinationSlot, what);
            if (amount <= 0) {
                continue;
            }

            var extracted = StorageHelper.poweredExtraction(energySource, storage, what, amount, getActionSource());
            if (extracted == 0) {
                return false; // No items available
            }

            var currentItem = destinationSlot.getItem();
            if (!currentItem.isEmpty()) {
                destinationSlot.setByPlayer(currentItem.copyWithCount(currentItem.getCount() + (int) extracted));
            } else {
                destinationSlot.setByPlayer(what.toStack((int) extracted));
            }
            return true;
        }

        return false;
    }
}
