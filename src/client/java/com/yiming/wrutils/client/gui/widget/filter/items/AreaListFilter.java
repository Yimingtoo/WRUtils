package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.AnyItem;
import com.yiming.wrutils.data.selected_area.SelectBoxes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AreaListFilter extends FilterTypeTemp {
    private final BlockFilterType blockType;
    private SelectBoxes oldSelectBoxes = null;

    public AreaListFilter(BlockFilterType blockType) {
        this.blockType = blockType;
        this.addItem(new AnyItem());
    }

    public AreaListFilter updateFilter(Collection<SelectBoxes> values) {
        Map<UUID, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getSelectBoxes().getUUID(), item.isChecked());
            }
        }
        this.clearExceptFirst();
        this.items.getFirst().setChecked(CheckState.CHECKED);
        for (SelectBoxes value : values) {
            Item item = new Item(this.blockType, value);
            CheckState checkState = oldMap.get(value.getUUID());
            if (checkState != null) {
                item.setChecked(checkState);
                if (checkState != CheckState.UNCHECKED) {
                    this.oldSelectBoxes = value;
                    this.items.getFirst().setChecked(CheckState.UNCHECKED);
                }
            } else {
                item.setChecked(CheckState.UNCHECKED);
            }
            this.addItem(item);
        }
        return this;
    }

    public SelectBoxes getOldSelectBoxes() {
        return this.oldSelectBoxes;
    }

    public static class Item extends FilterItem {
        private final BlockFilterType blockType;
        private final SelectBoxes selectBoxes;

        public Item(BlockFilterType blockType, SelectBoxes selectBoxes) {
            this.blockType = blockType;
            this.selectBoxes = selectBoxes;
        }

        public SelectBoxes getSelectBoxes() {
            return this.selectBoxes;
        }

        @Override
        public String getName() {
            return this.selectBoxes.getName();
        }
    }
}
