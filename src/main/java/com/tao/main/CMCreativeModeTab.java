package com.tao.main;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CMCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CompactCell.MODID);
    public static final Supplier<CreativeModeTab> EXAMPLE_TAB = DR.register("compactcell_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("压缩原件"))
                    .icon(() -> new ItemStack(CMItems.SPACE_ITEM_1K.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(CMItems.SPACE_ITEM_1K.get());
                        output.accept(CMItems.CMCELL_ITEM_CELL1K.get());
                        output.accept(CMItems.CMCELL_ITEM_CELL4K.get());
                        output.accept(CMItems.CMCELL_ITEM_CELL16K.get());
                        output.accept(CMItems.CMCELL_ITEM_CELL64K.get());
                        output.accept(CMItems.CMCELL_ITEM_CELL256K.get());
                        output.accept(CMItems.BONDER_ITEM.get());

                        output.accept(CMItems.SMOOTH_STONE_BLOCK.get());
                        output.accept(CMItems.CONCRETE_BLOCK.get());
                        output.accept(CMItems.INTERFACE_BLOCK.get());
                    })
                    .build()
    );
}
