package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.selected_area.SelectBoxes;

import java.util.ArrayList;

public class AreaListItem implements FilterType<SelectBoxes> {
    private SelectBoxes selectBoxes;

    public AreaListItem(SelectBoxes selectBoxes) {
        this.selectBoxes = selectBoxes;
    }


    @Override
    public SelectBoxes getValue() {
        return this.selectBoxes;
    }

    @Override
    public String getName() {
        return this.selectBoxes.getName();
    }

    @Override
    public void setValue(SelectBoxes value) {
        this.selectBoxes = value;
    }

    public static ArrayList<AreaListItem> getAreaListItems(ArrayList<SelectBoxes> selectBoxesList) {
        ArrayList<AreaListItem> areaListItems = new ArrayList<>();
        for (SelectBoxes selectBoxes : selectBoxesList) {
            areaListItems.add(new AreaListItem(selectBoxes));
        }
        return areaListItems;
    }
}
