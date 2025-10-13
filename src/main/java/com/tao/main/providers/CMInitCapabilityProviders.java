package com.tao.main.providers;

import appeng.api.AECapabilities;
import com.tao.main.CMBlockEntities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CMInitCapabilityProviders {
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, CMBlockEntities.INTERFACE.get(), (object, context) -> object);
    }
}
