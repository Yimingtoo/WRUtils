package com.yiming.wrutils.client.gui.widget.filter.items.base;

import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;

public class BooleanItem extends FilterItem {
    boolean value;

    public BooleanItem(boolean value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return String.valueOf(this.value);
    }

}
