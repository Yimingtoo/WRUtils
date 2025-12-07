package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.DataManager;

public class OriginTickItem extends LongItem {
    public OriginTickItem(long value) {
        super(value);
    }

    @Override
    public void setValue(long value) {
        super.setValue(value);
        DataManager.eventOriginTick = value;
    }

    @Override
    public FilterType copy() {
        return new OriginTickItem(getValue());
    }
}
