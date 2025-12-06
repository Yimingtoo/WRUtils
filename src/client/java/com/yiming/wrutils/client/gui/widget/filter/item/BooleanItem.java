package com.yiming.wrutils.client.gui.widget.filter.item;

import java.util.ArrayList;

public class BooleanItem implements FilterType {
    private boolean value;

    public BooleanItem(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }


    public static ArrayList<BooleanItem> booleanItems() {
        ArrayList<BooleanItem> list = new ArrayList<BooleanItem>();
        list.add(new BooleanItem(true));
        list.add(new BooleanItem(false));
        return list;
    }
}
