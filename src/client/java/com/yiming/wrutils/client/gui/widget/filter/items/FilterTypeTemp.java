package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.*;

public abstract class FilterTypeTemp {
    protected final List<FilterItem> items = new ArrayList<>();

    public List<FilterItem> getItems() {
        return this.items;
    }

    public void addItem(FilterItem item) {
        this.items.add(item);
    }

    public void removeItem(FilterItem item) {
        this.items.remove(item);
    }

    public void clear() {
        this.items.clear();
    }

    public void clearExceptFirst() {
        if (!this.items.isEmpty()) {
            this.items.subList(1, this.items.size()).clear();
        }
    }


    public boolean collectOrNotByFilter(BaseEvent event) {
        if (this.items.isEmpty()) {
            return true;
        }
        for (FilterItem item : this.items) {
            if (item.isChecked() != CheckState.UNCHECKED && item.collectOrNotByItem(event)) {
                return true;
            }
        }
        return false;
    }
}
