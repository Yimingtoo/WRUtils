package com.yiming.wrutils.client.gui.widget.filter.item;

import java.util.ArrayList;
import java.util.Collection;

public class LongItem implements FilterType {
    private long value;

    public LongItem(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

    public static ArrayList<LongItem> getIntegerItems(Collection<Long> values) {
        ArrayList<LongItem> items = new ArrayList<LongItem>();
        for (Long value : values) {
            items.add(new LongItem(value));
        }
        return items;
    }

}
