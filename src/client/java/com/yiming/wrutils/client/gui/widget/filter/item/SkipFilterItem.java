package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.BaseEvent;

public class SkipFilterItem implements FilterType {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        return true;
    }
}
