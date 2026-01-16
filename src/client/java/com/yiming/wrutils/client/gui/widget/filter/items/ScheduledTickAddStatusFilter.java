package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.BooleanItem;
import com.yiming.wrutils.client.gui.widget.filter.items.base.OtherItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;

public class ScheduledTickAddStatusFilter extends FilterTypeTemp {
    public ScheduledTickAddStatusFilter() {
        super();
        this.addItem(new SkippedItem());
        this.addItem(new Item(true));
        this.addItem(new Item(false));
    }

    public static class Item extends BooleanItem {
        public Item(boolean value) {
            super(value);
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            if (event instanceof ScheduledTickAddEvent event1) {
                if (event1.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.TRUE) {
                    return this.getValue();
                } else if (event1.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.FALSE) {
                    return !this.getValue();
                }
            }
            return false;
        }
    }

    public static class SkippedItem extends FilterItem {
        @Override
        public String getName() {
            return "Widely allowed";
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            if (!(event instanceof ScheduledTickAddEvent)) {
                return this.checkState != CheckState.UNCHECKED;
            }
            return false;
        }
    }

}
