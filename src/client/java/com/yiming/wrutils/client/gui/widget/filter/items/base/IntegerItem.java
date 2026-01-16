package com.yiming.wrutils.client.gui.widget.filter.items.base;

import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;

public class IntegerItem extends FilterItem {
    private int value;

    public IntegerItem(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

}
