package com.yiming.wrutils;

import com.yiming.wrutils.data.event.EventRecorder;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import com.yiming.wrutils.data.selected_area.AreaGroupManagement;
import net.fabricmc.api.ModInitializer;


public class Wrutils implements ModInitializer {
    public static final String MOD_ID = "wrutils-mod";
    public static AreaGroupManagement areaGroupManagement = new AreaGroupManagement();
    public static EventRecorder eventRecorder = new EventRecorder();

    @Override
    public void onInitialize() {
        areaGroupManagement.addAndSetCurrent(new SelectBoxes());

    }

    public static SelectBoxes getCurrentBoxes() {
        return areaGroupManagement.getCurrentBoxes();
    }

    public static SelectBox getCurrentSelectBox() {
        if (areaGroupManagement.getCurrentBoxes() != null) {
            return areaGroupManagement.getCurrentBoxes().getCurrentSelectBox();
        }
        return null;
    }

}
