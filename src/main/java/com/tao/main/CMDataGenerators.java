package com.tao.main;

import appeng.datagen.providers.tags.BlockTagsProvider;
import com.tao.main.dimension.CMInitBiomes;
import com.tao.main.dimension.CMInitDimensionTypes;
import com.tao.main.provider.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;


@EventBusSubscriber(modid = CompactCell.MODID)
public class CMDataGenerators {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        var pack = generator.getVanillaPack(true);
        var lookupProvider = event.getLookupProvider();
        // Worldgen et al
        pack.addProvider(packOutput -> new DatapackBuiltinEntriesProvider(output, lookupProvider, createDatapackEntriesBuilder(), Set.of(CompactCell.MODID)));

        var blockTagsProvider = pack.addProvider(packOutput -> new BlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
        pack.addProvider(packOutput -> new CMBlockStateProvider(output, existingFileHelper));
        pack.addProvider(packOutput -> new CMItemModelProvider(output, existingFileHelper));
        pack.addProvider(packOutput -> new CMTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        pack.addProvider(packOutput -> new CMRecipeProvider(output, lookupProvider));
        pack.addProvider(PackOutput-> new CMLanguageProvider(output));
    }

    private static RegistrySetBuilder createDatapackEntriesBuilder() {
        return new RegistrySetBuilder()
                .add(Registries.DIMENSION_TYPE, CMInitDimensionTypes::initDimension)
                .add(Registries.LEVEL_STEM, CMInitDimensionTypes::initLevel)
                .add(Registries.BIOME, CMInitBiomes::initBiomes);
    }
}