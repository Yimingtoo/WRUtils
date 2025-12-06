package com.yiming.wrutils.client.gui.widget.filter.dropdown;

import com.yiming.wrutils.client.gui.widget.filter.clickable.BaseClickableWidget;
import net.minecraft.text.Text;

public abstract class ExpandableClickableWidget extends BaseClickableWidget {
    protected boolean isExpanded = false;

    public ExpandableClickableWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }
}
