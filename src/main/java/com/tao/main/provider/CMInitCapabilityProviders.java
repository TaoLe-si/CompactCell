package com.tao.main.provider;

import appeng.api.AECapabilities;
import appeng.api.networking.IInWorldGridNodeHost;
import com.tao.main.CMBlockEntities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CMInitCapabilityProviders {
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AECapabilities.IN_WORLD_GRID_NODE_HOST, CMBlockEntities.INTERFACE.get(), (object, context) -> object);
    }
}
