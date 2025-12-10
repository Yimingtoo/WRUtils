package com.yiming.wrutils.client.gui.widget.filter.item.bool_item;

import com.yiming.wrutils.client.gui.widget.filter.item.int_item.IntegerItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;

import java.util.ArrayList;
import java.util.Collection;

public class ScheduledTickAddedStatusItem extends BooleanItem {
    public ScheduledTickAddedStatusItem(boolean value) {
        super(value);
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        if (event instanceof ScheduledTickAddEvent event1) {
            if (event1.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.TRUE) {
                return this.value;
            } else if (event1.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.FALSE) {
                return !this.value;
            }
        }
        return false;
    }

    public static ArrayList<ScheduledTickAddedStatusItem> getScheduledTickAddedStatusItems() {
        ArrayList<ScheduledTickAddedStatusItem> items = new ArrayList<>();
        items.add(new ScheduledTickAddedStatusItem(true));
        items.add(new ScheduledTickAddedStatusItem(false));
        return items;
    }

}
