package com.yiming.wrutils.client.gui.widget.filter.item.items.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.selected_area.SelectBox;

import java.util.ArrayList;

public class SubAreaItem implements FilterType<SelectBox> {
    private SelectBox selectBox;
    private BlockFilterType blockType;

    public SubAreaItem(SelectBox selectBox, BlockFilterType blockType) {
        this.selectBox = selectBox;
        this.blockType = blockType;
    }

    @Override
    public SelectBox getValue() {
        return this.selectBox;
    }

    @Override
    public String getName() {
        return this.selectBox.getName();
    }

    @Override
    public void setValue(SelectBox value) {
        this.selectBox = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        BlockInfo blockInfo = null;
        if (this.blockType == BlockFilterType.SOURCE) {
            blockInfo = event.getSourceBlockInfo();
        } else if (this.blockType == BlockFilterType.TARGET) {
            blockInfo = event.getTargetBlockInfo();
        }
        if (blockInfo != null) {
            return this.selectBox.containsVec3iPosOfDimension(blockInfo);
        }
        return false;
    }


    public static ArrayList<SubAreaItem> getSubAreaItems(ArrayList<SelectBox> selectBoxes, BlockFilterType blockType) {
        ArrayList<SubAreaItem> subAreaItems = new ArrayList<>();
        for (SelectBox selectBox : selectBoxes) {
            subAreaItems.add(new SubAreaItem(selectBox, blockType));
        }
        return subAreaItems;
    }
}
