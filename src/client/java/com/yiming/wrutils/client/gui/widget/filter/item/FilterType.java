package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.BaseEvent;

public interface FilterType<T> {
    T getValue();

    String getName();

    void setValue(T value);

    default boolean collectOrNot(BaseEvent event) {
        return true;
    }
    default void setValueFromText(String text) {
    }
}
