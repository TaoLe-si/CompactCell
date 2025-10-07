package com.tao.main;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.init.client.InitScreens;
import appeng.menu.me.common.MEStorageMenu;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class CMInitScreens {
    public static void init(RegisterMenuScreensEvent event) {
        InitScreens.<CMMEStorageMenu, MEStorageScreen<CMMEStorageMenu>>register(event,
                CMMEStorageMenu.SPACE_ITEM_CELL_TYPE,
                MEStorageScreen::new,
                "/screens/terminals/portable_item_cell.json");
    }
}
