package com.yiming.wrutils.client.gui.widget.filter;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.items.*;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.ArrayList;

public class FilterManager {

    public static ArrayList<FilterTypeTemp> filterManager = new ArrayList<>();

    public static GameTickFilter GAME_TICK_FILTER = (GameTickFilter) addFilter(new GameTickFilter());
    public static SequenceFilter SEQUENCE_FILTER = (SequenceFilter) addFilter(new SequenceFilter());
    public static EventTypeFilter EVENT_TYPE_FILTER = (EventTypeFilter) addFilter(new EventTypeFilter());
    public static DelayFilter DELAY_FILTER = (DelayFilter) addFilter(new DelayFilter());
    public static PriorityFilter PRIORITY_FILTER = (PriorityFilter) addFilter(new PriorityFilter());
    public static ScheduledTickAddStatusFilter SCHEDULED_TICK_ADD_STATUS_FILTER = (ScheduledTickAddStatusFilter) addFilter(new ScheduledTickAddStatusFilter());
    public static BlockFilter BLOCK_FILTER_SOURCE = (BlockFilter) addFilter(new BlockFilter(BlockFilterType.SOURCE));
    public static BlockFilter BLOCK_FILTER_TARGET = (BlockFilter) addFilter(new BlockFilter(BlockFilterType.TARGET));
    public static AreaListFilter AREA_LIST_FILTER_SOURCE = (new AreaListFilter(BlockFilterType.SOURCE));
    public static AreaListFilter AREA_LIST_FILTER_TARGET = (new AreaListFilter(BlockFilterType.TARGET));
    public static SubAreaFilter SUB_AREA_FILTER_SOURCE = (SubAreaFilter) addFilter(new SubAreaFilter(BlockFilterType.SOURCE));
    public static SubAreaFilter SUB_AREA_FILTER_TARGET = (SubAreaFilter) addFilter(new SubAreaFilter(BlockFilterType.TARGET));

    public static FilterTypeTemp addFilter(FilterTypeTemp filter) {
        filterManager.add(filter);
        return filter;
    }


    public static ArrayList<BaseEvent> getFilteredEventList() {
        // 准备
        GameTickFilter.setRelativeTick(GAME_TICK_FILTER.getOriginalTickItem().getValue());

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
