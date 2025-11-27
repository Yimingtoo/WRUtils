package com.yiming.wrutils;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.api.ModInitializer;


public class Wrutils implements ModInitializer {
    public static final String MOD_ID = "wrutils-mod";

    @Override
    public void onInitialize() {
        DataManager.areaGroupManagement.addAndSetCurrent(new SelectBoxes());

    }

}
