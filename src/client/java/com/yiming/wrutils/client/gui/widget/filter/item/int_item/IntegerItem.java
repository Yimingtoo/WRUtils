package com.yiming.wrutils.client.gui.widget.filter.item.int_item;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;

import java.util.ArrayList;
import java.util.Collection;

public class IntegerItem implements FilterType<Integer> {
    protected int value;

    public IntegerItem(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    public static ArrayList<IntegerItem> getIntegerItems(Collection<Integer> values) {
        ArrayList<IntegerItem> items = new ArrayList<IntegerItem>();
        for (Integer value : values) {
            items.add(new IntegerItem(value));
        }
        return items;
    }

}
