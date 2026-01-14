package com.yiming.wrutils.client.gui.widget.filter;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterTypeTemp;
import com.yiming.wrutils.client.gui.widget.filter.items.GameTickFilter;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.ArrayList;

public class FilterManager {

    public static ArrayList<FilterTypeTemp> filterManager = new ArrayList<>();

    public static FilterTypeTemp GAME_TICK_FILTER = addFilter(new GameTickFilter());

    public static FilterTypeTemp addFilter(FilterTypeTemp filter) {
        filterManager.add(filter);
        return filter;
    }


    public static ArrayList<BaseEvent> getFilteredEventList() {
        ArrayList<BaseEvent> filterEventList = new ArrayList<>();
        for (BaseEvent event : DataManager.eventRecorder) {
            boolean collect = true;
            for (FilterTypeTemp filter : filterManager) {
                if (!filter.collectOrNotByFilter(event)) {
                    collect = false;
                    break;
                }
            }
            if (collect) {
                filterEventList.add(event);
            }
        }
        return filterEventList;
    }
}
