package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.selected_area.SelectBoxes;

import java.util.ArrayList;

public class AreaListItem implements FilterType {
    private SelectBoxes selectBoxes;

    public AreaListItem(SelectBoxes selectBoxes) {
        this.selectBoxes = selectBoxes;
    }

    public SelectBoxes getSelectBoxes() {
        return this.selectBoxes;
    }

    @Override
    public String getName() {
        return this.selectBoxes.getName();
    }

    public static ArrayList<AreaListItem> getAreaListItems(ArrayList<SelectBoxes> selectBoxesList) {
        ArrayList<AreaListItem> areaListItems = new ArrayList<>();
        for (SelectBoxes selectBoxes : selectBoxesList) {
            areaListItems.add(new AreaListItem(selectBoxes));
        }
        return areaListItems;
    }
}
