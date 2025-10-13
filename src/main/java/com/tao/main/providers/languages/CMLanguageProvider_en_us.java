package com.tao.main.providers.languages;

import com.tao.main.CMItems;
import com.tao.main.CompactCell;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CMLanguageProvider_en_us extends LanguageProvider {
    public CMLanguageProvider_en_us(PackOutput output) {
        super(output, CompactCell.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(CMItems.SPACE_ITEM_1K.get(), "Cell Viewer");
        add(CMItems.CMCELL_ITEM_CELL1K.get(), "1k Space Cell");
        add(CMItems.CMCELL_ITEM_CELL4K.get(), "4k Space Cell");
        add(CMItems.CMCELL_ITEM_CELL16K.get(), "16k Space Cell");
        add(CMItems.CMCELL_ITEM_CELL64K.get(), "64k Space Cell");
        add(CMItems.CMCELL_ITEM_CELL256K.get(), "256k Space Cell");
        add(CMItems.BONDER_ITEM.get(), "Bonder");

        add(CMItems.SMOOTH_STONE_BLOCK.get(), "Smooth Stone");
        add(CMItems.CONCRETE_BLOCK.get(), "White Concrete");
        add(CMItems.INTERFACE_BLOCK.get(), "Super IO Interface");

        add("space_cell_id", "Cell id: ");
    }
}
