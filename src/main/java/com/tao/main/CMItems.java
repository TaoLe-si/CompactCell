package com.tao.main;

import appeng.api.ids.AECreativeTabIds;
import appeng.api.stacks.AEKeyType;
import appeng.core.MainCreativeTab;
import appeng.core.definitions.ItemDefinition;
import appeng.items.storage.StorageTier;
import com.tao.main.items.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class CMItems {
    public static final DeferredRegister.Items DR = DeferredRegister.createItems(CompactCell.MODID);

    public static final ItemDefinition<CMPortableSpaceCellItem> SPACE_ITEM_1K = makePortableItemCell(ResourceLocation.fromNamespaceAndPath("compactcell", "space_1k"), StorageTier.SIZE_1K);
    public static final DeferredItem<CMCellItem_1k> CMCELL_ITEM_CELL1K = DR.registerItem("cell_1k", CMCellItem_1k::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<CMCellItem_4k> CMCELL_ITEM_CELL4K = DR.registerItem("cell_4k", CMCellItem_4k::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<CMCellItem_16k> CMCELL_ITEM_CELL16K = DR.registerItem("cell_16k", CMCellItem_16k::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<CMCellItem_64k> CMCELL_ITEM_CELL64K = DR.registerItem("cell_64k", CMCellItem_64k::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<CMCellItem_256k> CMCELL_ITEM_CELL256K = DR.registerItem("cell_256k", CMCellItem_256k::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> BONDER_ITEM = DR.registerItem("bonder", Item::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<BlockItem> SMOOTH_STONE_BLOCK = DR.registerItem("smooth_stone", properties -> new BlockItem(CMBlocks.SMOOTH_STONE_UNBREAKABLE.get(), properties));
    public static final DeferredItem<BlockItem> CONCRETE_BLOCK = DR.registerItem("concrete_white", properties -> new BlockItem(CMBlocks.CONCRETE_UNBREAKABLE.get(), properties));
    public static final DeferredItem<BlockItem> INTERFACE_BLOCK = DR.registerItem("interface", properties -> new BlockItem(CMBlocks.INTERFACE_BLOCK.get(), properties));

    private static ItemDefinition<CMPortableSpaceCellItem> makePortableItemCell(ResourceLocation id, StorageTier tier) {
        var name = tier.namePrefix() + " Portable Item Cell";
        return item(name, id, p -> new CMPortableSpaceCellItem(AEKeyType.items(), 63 - tier.index() * 9, CMMEStorageMenu.SPACE_ITEM_CELL_TYPE, tier, p.stacksTo(1), 0x80caff));
    }

    static <T extends Item> ItemDefinition<T> item(String name, ResourceLocation id, Function<Item.Properties, T> factory) {
        return item(name, id, factory, AECreativeTabIds.MAIN);
    }

    static <T extends Item> ItemDefinition<T> item(String name, ResourceLocation id, Function<Item.Properties, T> factory, @Nullable ResourceKey<CreativeModeTab> group) {

        var definition = new ItemDefinition<>(name, DR.registerItem(id.getPath(), factory));

        if (Objects.equals(group, AECreativeTabIds.MAIN)) {
            MainCreativeTab.add(definition);
        } else if (group != null) {
            MainCreativeTab.add(definition);
            MainCreativeTab.addExternal(group, definition);
        }


        return definition;
    }
}
