package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.IntegerItem;
import com.yiming.wrutils.client.gui.widget.filter.items.base.OtherItem;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickInfo;
import net.minecraft.world.tick.TickPriority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PriorityFilter extends FilterTypeTemp {
    public PriorityFilter() {
        super();
        this.addItem(new OtherItem());
    }

    public PriorityFilter updateFilter(Collection<TickPriority> values) {
        Map<TickPriority, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getTickPriority(), item.isChecked());
            }
        }
        this.clearExceptFirst();
        for (TickPriority value : values) {
            Item item = new Item(value);
            CheckState checkState = oldMap.get(value);
            if (checkState != null) {
                item.setChecked(checkState);
            }
            this.addItem(item);
        }
        return this;
    }

    public static class Item extends FilterItem {
        private final TickPriority priority;

        public Item(TickPriority priority) {
            this.priority = priority;
        }

        public TickPriority getTickPriority() {
            return priority;
        }

        @Override
        public String getName() {
            return this.priority.name();
        }


        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            if (event instanceof ScheduledTickInfo info) {
                return info.getPriority() == this.priority;
            }
            return false;
        }
    }
}
