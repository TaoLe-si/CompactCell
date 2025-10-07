package com.tao.main.blocks;

import appeng.api.networking.IGridNodeListener;
import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.storage.DriveBlockEntity;
import appeng.me.GridNode;
import appeng.me.helpers.IGridConnectedBlockEntity;
import com.tao.main.CompactCell;
import com.tao.main.blockentity.CMInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CMInterfaceBlock extends AEBaseEntityBlock<CMInterfaceBlockEntity> {

    public enum InterfaceBlockState implements StringRepresentable {
        offline, online, conflicted;

        @Override
        public @NotNull String getSerializedName() {
            return this.name();
        }

    }
    public CMInterfaceBlock() {
        super(metalProps().strength(-1.0f, 3600000.0f));
        this.registerDefaultState(this.defaultBlockState().setValue(INTERFACE_STATE, InterfaceBlockState.offline));
    }
    public static final EnumProperty<InterfaceBlockState> INTERFACE_STATE = EnumProperty.create("state", InterfaceBlockState.class);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(INTERFACE_STATE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    public IOrientationStrategy getOrientationStrategy() {
        return OrientationStrategies.none();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        if (level instanceof Level realLevel && !realLevel.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CMInterfaceBlockEntity interfaceBE) {
                if (interfaceBE.getMainNode() != null) {
                    interfaceBE.updateState();
                } else {
                    interfaceBE.onReady();
                }
            }
        }
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            var from = level.getBlockEntity(fromPos);
            if (be instanceof CMInterfaceBlockEntity interfaceBE) {
                if (interfaceBE.getMainNode() != null) {
                    interfaceBE.updateState();
                } else {
                    interfaceBE.onReady();
                }
            }
        }
    }
}
