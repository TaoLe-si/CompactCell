package com.tao.main.providers;

import com.tao.main.CMBlocks;
import com.tao.main.CompactCell;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CMBlockStateProvider extends BlockStateProvider {
    public CMBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CompactCell.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CMBlocks.SMOOTH_STONE_UNBREAKABLE.get(), models().withExistingParent("smooth_stone_unbreakable", "block/smooth_stone"));
        simpleBlock(CMBlocks.CONCRETE_UNBREAKABLE.get(), models().withExistingParent("concrete_unbreakable", "block/white_concrete"));
        simpleBlock(CMBlocks.INTERFACE_BLOCK.get(), models().cubeAll("interface", modLoc("block/interface")));
    }
}
