package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.data.event.BaseEvent;

public interface FilterItem {

    String getName();

    boolean isChecked();

    void setChecked(boolean checked);

    default boolean collectOrNotByItem(BaseEvent event) {
        return false;
    }

    default void setValueFromText(String text) {
    }
}
