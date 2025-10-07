package com.tao.main;

import com.tao.main.blocks.CMCONCRETE_WHITE;
import com.tao.main.blocks.CMInterfaceBlock;
import com.tao.main.blocks.CMSMOOTH_STONE;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CMBlocks {
    public static final DeferredRegister.Blocks DR = DeferredRegister.createBlocks(CompactCell.MODID);
    public static final DeferredBlock<CMSMOOTH_STONE> SMOOTH_STONE_UNBREAKABLE = DR.register("smooth_stone_unbreakable", CMSMOOTH_STONE::new);
    public static final DeferredBlock<CMCONCRETE_WHITE> CONCRETE_UNBREAKABLE = DR.register("concrete_white_unbreakable", CMCONCRETE_WHITE::new);
    public static final DeferredBlock<CMInterfaceBlock> INTERFACE_BLOCK = DR.register("interface", CMInterfaceBlock::new);

}
