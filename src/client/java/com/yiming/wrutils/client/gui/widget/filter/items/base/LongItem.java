package com.yiming.wrutils.client.gui.widget.filter.items.base;

import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;

public class LongItem extends FilterItem {
    private long value;

    public LongItem(long value) {
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

}
