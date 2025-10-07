package com.tao.main.provider;

import com.tao.main.CMItems;
import com.tao.main.CompactCell;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class CMItemModelProvider extends ItemModelProvider {
    public CMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CompactCell.MODID, existingFileHelper);
    }
    @Override
    protected void registerModels() {
        withExistingParent(CMItems.SPACE_ITEM_1K.id().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");

        withExistingParent(CMItems.CMCELL_ITEM_CELL1K.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");
        withExistingParent(CMItems.CMCELL_ITEM_CELL4K.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");
        withExistingParent(CMItems.CMCELL_ITEM_CELL16K.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");
        withExistingParent(CMItems.CMCELL_ITEM_CELL64K.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");
        withExistingParent(CMItems.CMCELL_ITEM_CELL256K.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/cell");

        withExistingParent(CMItems.BONDER_ITEM.getId().getPath(), mcLoc("item/generated")).texture("layer0", "item/bonder");

        withExistingParent(CMItems.SMOOTH_STONE_BLOCK.getId().getPath(), mcLoc("block/smooth_stone"));
        withExistingParent(CMItems.CONCRETE_BLOCK.getId().getPath(), mcLoc("block/white_concrete"));
        withExistingParent(CMItems.INTERFACE_BLOCK.getId().getPath(), modLoc("block/interface"));

    }
}
