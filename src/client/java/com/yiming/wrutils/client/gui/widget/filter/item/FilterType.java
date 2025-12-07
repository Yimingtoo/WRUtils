package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.BaseEvent;

public interface FilterType {
    String getName();

    default boolean collectOrNot(BaseEvent event) {
        return true;
    }

    default void setData(String text) {
    }

    default FilterType copy() {
        return this;
    }
}
