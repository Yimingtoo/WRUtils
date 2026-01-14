package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.ArrayList;
import java.util.List;

public interface FilterTypeTemp {
    List<? extends FilterItem> getItems();

    default boolean collectOrNotByFilter(BaseEvent event) {
        for (FilterItem item : this.getItems()) {
            if (item.isChecked() && !item.collectOrNotByItem(event)) {
                return false;
            }
        }
        return true;
    }
}
