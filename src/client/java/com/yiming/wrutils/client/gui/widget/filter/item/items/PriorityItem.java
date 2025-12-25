package com.yiming.wrutils.client.gui.widget.filter.item.items;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickInfo;
import net.minecraft.world.tick.TickPriority;

import java.util.ArrayList;

public class PriorityItem implements FilterType<TickPriority> {
    private TickPriority priority;

    public PriorityItem(TickPriority priority) {
        this.priority = priority;
    }

    @Override
    public TickPriority getValue() {
        return this.priority;
    }

    @Override
    public String getName() {
        return priority.name();
    }

    @Override
    public void setValue(TickPriority value) {
        this.priority = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        if (event instanceof ScheduledTickInfo info) {
            return info.getPriority() == this.priority;
        }
        return false;
    }

    public static ArrayList<PriorityItem> priorityItems() {
        ArrayList<PriorityItem> priorityItems = new ArrayList<>();
        for (TickPriority priority : TickPriority.values()) {
            priorityItems.add(new PriorityItem(priority));
        }
        return priorityItems;
    }
}
