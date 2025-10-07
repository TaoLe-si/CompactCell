package com.tao.main.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class CMCONCRETE_WHITE extends Block {
    public CMCONCRETE_WHITE() {
        super(Block.Properties.of()
                .strength(-1.0f, 3600000.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE));
    }
}
