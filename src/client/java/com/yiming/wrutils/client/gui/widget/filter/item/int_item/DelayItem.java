package com.yiming.wrutils.client.gui.widget.filter.item.int_item;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import com.yiming.wrutils.data.event.ScheduledTickInfo;

import java.util.ArrayList;
import java.util.Collection;

public class DelayItem extends IntegerItem {
    public DelayItem(int value) {
        super(value);
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        if (event instanceof ScheduledTickInfo info) {
            return info.getDelay() == this.value;
        }
        return false;
    }
    public static ArrayList<DelayItem> getDelayItems(Collection<Integer> values) {
        ArrayList<DelayItem> items = new ArrayList<>();
        for (Integer value : values) {
            items.add(new DelayItem(value));
        }
        return items;
    }


}
