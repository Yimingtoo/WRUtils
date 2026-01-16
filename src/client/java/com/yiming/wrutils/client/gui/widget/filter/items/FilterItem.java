package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.data.event.BaseEvent;

public abstract class FilterItem {
    protected CheckState checkState = CheckState.CHECKED;

    public CheckState isChecked() {
        return checkState;
    }

    public void setChecked(CheckState checkState) {
        this.checkState = checkState;
    }

    public abstract String getName();



    public boolean collectOrNotByItem(BaseEvent event) {
        return false;
    }

    public void setValueFromText(String text) {
    }
}
