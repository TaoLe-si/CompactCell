package com.tao.main.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class CMSMOOTH_STONE extends Block  {
    public CMSMOOTH_STONE() {
        super(Block.Properties.of()
                .strength(-1.0f, 3600000.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE));
    }
}
