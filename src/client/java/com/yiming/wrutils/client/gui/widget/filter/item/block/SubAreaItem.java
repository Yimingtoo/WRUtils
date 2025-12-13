package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.selected_area.SelectBox;
import net.minecraft.util.math.BlockPos;

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
        BlockPos pos = null;
        if (this.blockType == BlockFilterType.SOURCE) {
            pos = event.getSourceBlockInfo().pos();
        } else if (this.blockType == BlockFilterType.TARGET) {
            pos = event.getTargetBlockInfo().pos();
        }
        if (pos != null) {
            return this.selectBox.isContainVec3iPos(pos);
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
