package com.yiming.wrutils.data.select_box;

import java.util.ArrayList;

public class SelectBoxes {
    public static ArrayList<SelectBox> selectBoxes = new ArrayList<>();
    public static int index = 0;

    public static void add(SelectBox selectBox) {
        selectBoxes.add(selectBox);
    }

    public static SelectBox get(int index) {
        return selectBoxes.get(index);
    }
    public static SelectBox getCurrent() {
        return selectBoxes.get(index);
    }

}
