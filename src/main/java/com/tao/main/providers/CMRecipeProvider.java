package com.tao.main.providers;

import appeng.datagen.providers.tags.ConventionTags;
import com.tao.main.CMItems;
import com.tao.main.CompactCell;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CMRecipeProvider extends RecipeProvider {
    public CMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }
    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.SPACE_ITEM_1K.get())
                .pattern("*A*")
                .pattern("BXB")
                .pattern("*A*")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_1k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_2")))
                .define('X', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "item_cell_housing")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.CMCELL_ITEM_CELL1K.get())
                .pattern("***")
                .pattern("BAB")
                .pattern("***")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_1k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_2")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.CMCELL_ITEM_CELL4K.get())
                .pattern("***")
                .pattern("BAB")
                .pattern("***")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_4k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_16")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.CMCELL_ITEM_CELL16K.get())
                .pattern("***")
                .pattern("BAB")
                .pattern("***")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_16k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_16")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.CMCELL_ITEM_CELL64K.get())
                .pattern("***")
                .pattern("BAB")
                .pattern("***")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_64k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_128")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMItems.CMCELL_ITEM_CELL256K.get())
                .pattern("***")
                .pattern("BAB")
                .pattern("***")
                .define('A', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "cell_component_256k")))
                .define('B', BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "spatial_cell_component_128")))
                .define('*', Items.ENDER_PEARL)
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CMItems.BONDER_ITEM.get())
                .requires(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ae2", "certus_quartz_wrench")))
                .group(CompactCell.MODID)
                .unlockedBy("has_certus_quartz_dust", has(ConventionTags.CERTUS_QUARTZ_DUST))
                .save(output);
    }
}
