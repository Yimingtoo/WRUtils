package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.IntegerItem;
import com.yiming.wrutils.client.gui.widget.filter.items.base.OtherItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickInfo;

import java.util.*;

public class DelayFilter extends FilterTypeTemp {
    public DelayFilter() {
        super();
        this.addItem(new OtherItem());
    }

    public DelayFilter updateFilter(Collection<Integer> values) {
        Map<Integer, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getValue(), item.isChecked());
            }
        }
        this.clearExceptFirst();
        for (int value : values) {
            Item item = new Item(value);
            CheckState checkState = oldMap.get(value);
            if (checkState != null) {
                item.setChecked(checkState);
            }
            this.addItem(item);
        }
        return this;
    }

    public static class Item extends IntegerItem {
        public Item(int value) {
            super(value);
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            if (event instanceof ScheduledTickInfo info) {
                return info.getDelay() == this.getValue();
            }
            return false;
        }
    }
}
