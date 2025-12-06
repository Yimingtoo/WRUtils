package com.yiming.wrutils.client.gui.widget.filter.item;

import java.util.ArrayList;
import java.util.Collection;

public class IntegerItem implements FilterType {
    private int value;

    public IntegerItem(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

    public static ArrayList<IntegerItem> getIntegerItems(Collection<Integer> values) {
        ArrayList<IntegerItem> items = new ArrayList<IntegerItem>();
        for (Integer value : values) {
            items.add(new IntegerItem(value));
        }
        return items;
    }

}
