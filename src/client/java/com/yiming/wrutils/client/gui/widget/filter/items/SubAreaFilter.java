package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.AnyItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.selected_area.SelectBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SubAreaFilter extends FilterTypeTemp {
    private final BlockFilterType blockType;

    public SubAreaFilter(BlockFilterType blockType) {
        this.blockType = blockType;
    }

    public SubAreaFilter updateFilter(Collection<SelectBox> values) {
        Map<SelectBox, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getSelectBox(), item.isChecked());
            }
        }
        this.clear();
        for (SelectBox value : values) {
            Item item = new Item(this.blockType, value);
            CheckState checkState = oldMap.get(value);
            if (checkState != null) {
                item.setChecked(checkState);
            }
            this.addItem(item);
        }
        return this;
    }

    public BlockFilterType getBlockType() {
        return this.blockType;
    }

    public static class Item extends FilterItem {
        private final BlockFilterType blockType;
        private final SelectBox selectBox;

        public Item(BlockFilterType blockType, SelectBox selectBox) {
            this.blockType = blockType;
            this.selectBox = selectBox;
        }

        public SelectBox getSelectBox() {
            return this.selectBox;
        }

        protected BlockInfo getEventBlockInfo(BaseEvent event) {
            return switch (this.blockType) {
                case BlockFilterType.SOURCE -> event.getSourceBlockInfo();
                case BlockFilterType.TARGET -> event.getTargetBlockInfo();
            };
        }

        @Override
        public String getName() {
            return this.selectBox.getName();
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            BlockInfo blockInfo = this.getEventBlockInfo(event);
            if (blockInfo != null) {
                return this.selectBox.containsVec3iPosOfDimension(blockInfo);
            }
            return false;
        }
    }
}
