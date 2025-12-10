package com.yiming.wrutils.client.gui.widget.filter.item.long_item;

import com.yiming.wrutils.data.DataManager;

public class OriginTickItem extends LongItem {
    public OriginTickItem(long value) {
        super(value);
    }

    @Override
    public void setValue(Long value) {
        super.setValue(value);
        DataManager.eventOriginTick = value;
    }

}