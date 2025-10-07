package com.tao.main;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.ClientTickingBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.storage.DriveBlockEntity;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.DeferredBlockEntityType;
import appeng.core.definitions.ItemDefinition;
import com.google.common.base.Preconditions;
import com.tao.main.blockentity.CMInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class CMBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> DR = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CompactCell.MODID);
    public static final DeferredBlockEntityType<CMInterfaceBlockEntity> INTERFACE = create("interface", CMInterfaceBlockEntity.class, CMInterfaceBlockEntity::new, new BlockDefinition<>("interface", CMBlocks.INTERFACE_BLOCK,  new ItemDefinition<>("interface", CMItems.INTERFACE_BLOCK)));

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private static <T extends AEBaseBlockEntity> DeferredBlockEntityType<T> create(String shortId, Class<T> entityClass, BlockEntityFactory<T> factory, BlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefinitions) {
        Preconditions.checkArgument(blockDefinitions.length > 0);
        var deferred = DR.register(shortId, () -> {
            AtomicReference<BlockEntityType<T>> typeHolder = new AtomicReference<>();
            BlockEntityType.BlockEntitySupplier<T> supplier = (blockPos, blockState) -> factory.create(typeHolder.get(), blockPos, blockState);
            var blocks = Arrays.stream(blockDefinitions).map(BlockDefinition::block).toArray(AEBaseEntityBlock[]::new);
            var type = BlockEntityType.Builder.of(supplier, blocks).build(null);
            typeHolder.setPlain(type); // Makes it available to the supplier used above
            AEBaseBlockEntity.registerBlockEntityItem(type, blockDefinitions[0].asItem());
            BlockEntityTicker<T> serverTicker = null;
            if (ServerTickingBlockEntity.class.isAssignableFrom(entityClass)) {
                serverTicker = (level, pos, state, entity) -> {
                    ((ServerTickingBlockEntity) entity).serverTick();
                };
            }
            BlockEntityTicker<T> clientTicker = null;
            if (ClientTickingBlockEntity.class.isAssignableFrom(entityClass)) {
                clientTicker = (level, pos, state, entity) -> {
                    ((ClientTickingBlockEntity) entity).clientTick();
                };
            }
            for (var block : blocks) {
                AEBaseEntityBlock<T> baseBlock = (AEBaseEntityBlock<T>) block;
                baseBlock.setBlockEntity(entityClass, type, clientTicker, serverTicker);
            }
            return type;
        });

        return new DeferredBlockEntityType<>(entityClass, deferred);
    }
    @FunctionalInterface
    interface BlockEntityFactory<T extends AEBaseBlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }
}
