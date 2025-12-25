package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.client.gui.widget.filter.item.items.EventTypeItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.GameTickItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.PriorityItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.SequenceItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.AreaListItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.BlockItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.DimensionItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.SubAreaItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.bool_item.ScheduledTickAddedStatusItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.int_item.DelayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterTypeList {
    public Map<ItemType, ArrayList<? extends FilterType<?>>> filterTypeList = new HashMap<>();
    public ArrayList<EventTypeItem> EVENT_TYPE_LIST = new ArrayList<>();
    public ArrayList<GameTickItem> GAME_TICK_LIST = new ArrayList<>();
    public ArrayList<PriorityItem> PRIORITY_LIST = new ArrayList<>();
    public ArrayList<SequenceItem> SEQUENCE_LIST = new ArrayList<>();
    public ArrayList<AreaListItem> SOURCE_AREA_LIST_LIST = new ArrayList<>();
    public ArrayList<BlockItem> SOURCE_BLOCK_LIST = new ArrayList<>();
    public ArrayList<AreaListItem> TARGET_AREA_LIST_LIST = new ArrayList<>();
    public ArrayList<BlockItem> TARGET_BLOCK_LIST = new ArrayList<>();
    public ArrayList<DimensionItem> DIMENSION_LIST = new ArrayList<>();
    public ArrayList<SubAreaItem> SUB_AREA_LIST = new ArrayList<>();
    public ArrayList<ScheduledTickAddedStatusItem> SCHEDULED_TICK_ADDED_STATUS_LIST = new ArrayList<>();
    public ArrayList<DelayItem> DELAY_LIST = new ArrayList<>();

    public FilterTypeList() {
        this.filterTypeList.put(ItemType.EVENT_TYPE,EVENT_TYPE_LIST);
        this.filterTypeList.put(ItemType.GAME_TICK,GAME_TICK_LIST);
        this.filterTypeList.put(ItemType.PRIORITY,PRIORITY_LIST);
        this.filterTypeList.put(ItemType.SEQUENCE,SEQUENCE_LIST);
        this.filterTypeList.put(ItemType.SOURCE_AREA_LIST, SOURCE_AREA_LIST_LIST);
        this.filterTypeList.put(ItemType.SOURCE_BLOCK,SOURCE_BLOCK_LIST);
        this.filterTypeList.put(ItemType.TARGET_AREA_LIST, TARGET_AREA_LIST_LIST);
        this.filterTypeList.put(ItemType.TARGET_BLOCK,TARGET_BLOCK_LIST);
        this.filterTypeList.put(ItemType.DIMENSION,DIMENSION_LIST);
//        this.filterTypeList.put(ItemType.SOURCE_AREA_LIST,SUB_AREA_LIST);
        this.filterTypeList.put(ItemType.SCHEDULED_TICK_ADDED_STATUS,SCHEDULED_TICK_ADDED_STATUS_LIST);
        this.filterTypeList.put(ItemType.DELAY,DELAY_LIST);
    }


//    public void clear() {
//        for (ArrayList<? extends FilterType<?>> filterType : filterTypeList) {
//            filterType.clear();
//        }
//    }


}
