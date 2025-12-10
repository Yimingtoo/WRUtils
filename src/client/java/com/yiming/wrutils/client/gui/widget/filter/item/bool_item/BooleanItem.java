package com.yiming.wrutils.client.gui.widget.filter.item.bool_item;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;

import java.util.ArrayList;

public class BooleanItem implements FilterType<Boolean> {
    protected boolean value;

    public BooleanItem(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }


    public static ArrayList<BooleanItem> booleanItems() {
        ArrayList<BooleanItem> list = new ArrayList<BooleanItem>();
        list.add(new BooleanItem(true));
        list.add(new BooleanItem(false));
        return list;
    }
}
