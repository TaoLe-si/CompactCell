package com.tao.main;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CMTags {
    public static final TagKey<Item> SPACE_CELL = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("compactcell", "space_cell"));
    public static final TagKey<Item> WRENCH =  TagKey.create(Registries.ITEM, ResourceLocation.parse("c:tools/wrench"));
}
