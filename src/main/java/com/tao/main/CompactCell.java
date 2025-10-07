package com.tao.main;

import com.tao.main.events.*;
import com.tao.main.provider.CMInitCapabilityProviders;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(CompactCell.MODID)
public class CompactCell {
    public static final String MODID = "compactcell";
    public static final Logger LOG = LoggerFactory.getLogger(CompactCell.class);
    public CompactCell(IEventBus modBus) {

        modBus.addListener(CMInitScreens::init);
        modBus.addListener(CMColorHandlersEvent::registerItemColors);
        modBus.addListener(CMInitCapabilityProviders::register);
        //CMServerPlayer
        NeoForge.EVENT_BUS.addListener(CMServerPlayer::onPlayerLogin);
        NeoForge.EVENT_BUS.addListener(CMServerPlayer::onPlayerDisconnected);
        NeoForge.EVENT_BUS.addListener(CMServerPlayer::onPlayerRespawn);

        NeoForge.EVENT_BUS.addListener(CMPlayerUseBlockEvent::onPlayerUseBlockEvent);
        NeoForge.EVENT_BUS.addListener(CMPlayerChangedDimensionEvent::onPlayerChangedDimension);
        //NeoForge.EVENT_BUS.addListener(new CMPlayerChangedDimension());

        CMItems.DR.register(modBus);
        CMBlocks.DR.register(modBus);
        CMBlockEntities.DR.register(modBus);
        CMCreativeModeTab.DR.register(modBus);
        CMDataComponents.DR.register(modBus);

        LOG.info("{} 初始化完成", MODID);
    }
}
