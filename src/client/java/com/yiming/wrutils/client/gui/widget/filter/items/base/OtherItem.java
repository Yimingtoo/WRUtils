package com.yiming.wrutils.client.gui.widget.filter.items.base;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import com.yiming.wrutils.data.event.ScheduledTickInfo;

public class OtherItem extends FilterItem {
    @Override
    public String getName() {
        return "Widely allowed";
    }

    @Override
    public boolean collectOrNotByItem(BaseEvent event) {
        if (!(event instanceof ScheduledTickInfo)) {
            return this.checkState != CheckState.UNCHECKED;
        }
        return false;
    }
}
