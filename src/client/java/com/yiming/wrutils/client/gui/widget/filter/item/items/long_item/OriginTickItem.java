package com.yiming.wrutils.client.gui.widget.filter.item.items.long_item;

import com.yiming.wrutils.client.data.DataManagerClient;

public class OriginTickItem extends LongItem {
    public OriginTickItem(long value) {
        super(value);
    }

    @Override
    public void setValue(Long value) {
        super.setValue(value);
        DataManagerClient.eventOriginTick = value;
    }

}