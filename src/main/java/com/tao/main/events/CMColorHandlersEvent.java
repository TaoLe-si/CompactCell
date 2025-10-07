package com.tao.main.events;

import appeng.core.definitions.ItemDefinition;
import com.tao.main.CMItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class CMColorHandlersEvent {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 0xFFF8F0FC, new ItemDefinition<>(CMItems.CMCELL_ITEM_CELL1K.getRegisteredName(), CMItems.CMCELL_ITEM_CELL1K));
        event.register((stack, tintIndex) -> 0xFFF3D9FA, new ItemDefinition<>(CMItems.CMCELL_ITEM_CELL4K.getRegisteredName(), CMItems.CMCELL_ITEM_CELL4K));
        event.register((stack, tintIndex) -> 0xFFf8deff, new ItemDefinition<>(CMItems.CMCELL_ITEM_CELL16K.getRegisteredName(), CMItems.CMCELL_ITEM_CELL16K));
        event.register((stack, tintIndex) -> 0xFFEEBEFA, new ItemDefinition<>(CMItems.CMCELL_ITEM_CELL64K.getRegisteredName(), CMItems.CMCELL_ITEM_CELL64K));
        event.register((stack, tintIndex) -> 0xFFEEBEFF, new ItemDefinition<>(CMItems.CMCELL_ITEM_CELL256K.getRegisteredName(), CMItems.CMCELL_ITEM_CELL256K));
    }
}
