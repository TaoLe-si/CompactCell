package com.tao.main.blockentity;

import appeng.api.config.Actionable;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.*;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridBootingStatusChange;
import appeng.api.networking.events.GridControllerChange;
import appeng.api.networking.events.GridEvent;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.orientation.BlockOrientation;
import appeng.api.util.AECableType;
import appeng.blockentity.grid.AENetworkedBlockEntity;
import appeng.blockentity.grid.AENetworkedPoweredBlockEntity;
import appeng.integration.modules.igtooltip.GridNodeState;
import appeng.me.GridNode;
import appeng.util.Platform;
import com.tao.main.CMConnectionsHelper;
import com.tao.main.CompactCell;
import com.tao.main.blocks.CMInterfaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CMInterfaceBlockEntity extends AENetworkedPoweredBlockEntity {
    private UUID mHolder;
    static {
        GridHelper.addNodeOwnerEventHandler(GridControllerChange.class, CMInterfaceBlockEntity.class, CMInterfaceBlockEntity::updateState);
    }
    public CMInterfaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.getMainNode().setIdlePowerUsage(0);
        this.getMainNode().setFlags(GridFlags.DENSE_CAPACITY);
        this.getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
        this.getMainNode().setInWorldNode(true);
    }

    @Override
    public InternalInventory getInternalInventory() {
        return InternalInventory.empty();
    }

    @Override
    public AECableType getCableConnectionType(Direction dir) {
        return AECableType.DENSE_SMART;
    }
    public void setHolder(UUID holder) {
        mHolder = holder;
        updateState();
    }
    public UUID getHolder() {
        return mHolder;
    }

    @Override
    public void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        if (mHolder != null) {
            data.putUUID("holder", mHolder);
        }
    }

    @Override
    public void loadTag(CompoundTag data, HolderLookup.Provider registries) {
        super.loadTag(data, registries);
        mHolder = data.getUUID("holder");
    }

    @Override
    public void onReady() {
        super.onReady();
        updateState();
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        this.updateState();
    }

    @Override
    public Set<Direction> getGridConnectableSides(BlockOrientation orientation) {
        return EnumSet.allOf(Direction.class);
    }

    public void updateState() {
        if (!this.getMainNode().isReady()) {
            return;
        }

        CMInterfaceBlock.InterfaceBlockState metaState = CMInterfaceBlock.InterfaceBlockState.offline;

        var grid = getMainNode().getGrid();
        if (grid != null) {

            metaState = CMInterfaceBlock.InterfaceBlockState.online;
            grid.getEnergyService().injectPower(200, Actionable.MODULATE);

        }
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CMInterfaceBlock.INTERFACE_STATE, metaState), Block.UPDATE_ALL);
        markForUpdate();
        CMConnectionsHelper.tryConnet(getHolder(), this.level, getGridNode());
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
    }

}
