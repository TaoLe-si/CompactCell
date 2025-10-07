package com.tao.main.provider;

import com.tao.main.CMBlocks;
import com.tao.main.CMItems;
import com.tao.main.CompactCell;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CMLanguageProvider extends LanguageProvider {
    public CMLanguageProvider(PackOutput output) {
        super(output, CompactCell.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(CMItems.SPACE_ITEM_1K.get(), "便携式压缩空间原件");
        add(CMItems.CMCELL_ITEM_CELL1K.get(), "1k 空间原件");
        add(CMItems.CMCELL_ITEM_CELL4K.get(), "4k 空间原件");
        add(CMItems.CMCELL_ITEM_CELL16K.get(), "16k 空间原件");
        add(CMItems.CMCELL_ITEM_CELL64K.get(), "64k 空间原件");
        add(CMItems.CMCELL_ITEM_CELL256K.get(), "256k 空间原件");
        add(CMItems.BONDER_ITEM.get(), "绑定工具");

        add(CMItems.SMOOTH_STONE_BLOCK.get(), "平滑石砖");
        add(CMItems.CONCRETE_BLOCK.get(), "白色混凝土");
        add(CMItems.INTERFACE_BLOCK.get(), "智能空间接口");

        add("space_cell_id", "空间原件id: ");
    }
}
