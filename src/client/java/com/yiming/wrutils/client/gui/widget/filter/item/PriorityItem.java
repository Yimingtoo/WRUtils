package com.yiming.wrutils.client.gui.widget.filter.item;

import net.minecraft.world.tick.TickPriority;

import java.util.ArrayList;

public class PriorityItem implements FilterType {
    private TickPriority priority;

    public PriorityItem(TickPriority priority) {
        this.priority = priority;
    }

    public TickPriority getPriority() {
        return priority;
    }

    @Override
    public String getName() {
        return priority.name();
    }

    public  static ArrayList<PriorityItem> priorityItems(){
        ArrayList<PriorityItem> priorityItems = new ArrayList<>();
        for(TickPriority priority : TickPriority.values()){
            priorityItems.add(new PriorityItem(priority));
        }
        return priorityItems;
    }
}
