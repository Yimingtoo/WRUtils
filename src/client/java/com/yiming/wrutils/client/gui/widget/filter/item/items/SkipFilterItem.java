package com.yiming.wrutils.client.gui.widget.filter.item.items;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;

public class SkipFilterItem implements FilterType<Boolean> {

    @Override
    public Boolean getValue() {
        return false;
    }

    @Override
    public String getName() {
        return "SkipFilterItem";
    }

    @Override
    public void setValue(Boolean value) {
    }


}
