package com.tao.main.items;

import appeng.api.stacks.AEKeyType;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import com.tao.main.CMDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CMPortableSpaceCellItem extends PortableCellItem {
    public CMPortableSpaceCellItem(AEKeyType keyType, int totalTypes, MenuType<?> menuType, StorageTier tier, Properties props, int defaultColor) {
        super(keyType, totalTypes, menuType, tier, props, defaultColor);
        //var id = stack.has(CMDataComponents.Pid) ? stack.get(CMDataComponents.Pid).id() : 0;
    }
}
