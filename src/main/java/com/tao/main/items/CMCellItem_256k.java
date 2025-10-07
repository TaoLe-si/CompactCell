package com.tao.main.items;

import com.tao.main.CMCellTiers;
import com.tao.main.CMDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CMCellItem_256k extends Item implements CMBaseCellItem {
    private final CMCellTiers tier = CMCellTiers.TIER_5;
    public CMCellItem_256k(Properties properties) {
        super(properties);
    }
    @Override
    public CMCellTiers getTier() {
        return tier;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        var id = stack.has(CMDataComponents.Pid) ? stack.get(CMDataComponents.Pid).id() : 0;
        tooltipComponents.add(Component.translatable("space_cell_id").append(" " + id).withStyle(ChatFormatting.BLUE));
    }
}
