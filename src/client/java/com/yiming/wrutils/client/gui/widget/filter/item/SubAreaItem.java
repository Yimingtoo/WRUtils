package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.selected_area.SelectBox;

import java.util.ArrayList;

public class SubAreaItem implements FilterType {
    private SelectBox selectBox;

    public SubAreaItem(SelectBox selectBox) {
        this.selectBox = selectBox;
    }

    public SelectBox getSelectBox() {
        return this.selectBox;
    }

    @Override
    public String getName() {
        return this.selectBox.getName();
    }

    public static ArrayList<SubAreaItem> getSubAreaItems(ArrayList<SelectBox> selectBoxes) {
        ArrayList<SubAreaItem> subAreaItems = new ArrayList<>();
        for (SelectBox selectBox : selectBoxes) {
            subAreaItems.add(new SubAreaItem(selectBox));
        }
        return subAreaItems;
    }
}
