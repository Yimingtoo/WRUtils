package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.selected_area.SelectBoxes;

import java.util.ArrayList;

public class AreaListItem implements FilterType<SelectBoxes> {
    private SelectBoxes selectBoxes;
    private BlockFilterType blockType;

    public AreaListItem(SelectBoxes selectBoxes, BlockFilterType blockType) {
        this.selectBoxes = selectBoxes;
        this.blockType = blockType;
    }

    public BlockFilterType getBlockType() {
        return this.blockType;
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

    public static ArrayList<AreaListItem> getAreaListItems(ArrayList<SelectBoxes> selectBoxesList, BlockFilterType blockType) {
        ArrayList<AreaListItem> areaListItems = new ArrayList<>();
        for (SelectBoxes selectBoxes : selectBoxesList) {
            areaListItems.add(new AreaListItem(selectBoxes, blockType));
        }
        return areaListItems;
    }
}
