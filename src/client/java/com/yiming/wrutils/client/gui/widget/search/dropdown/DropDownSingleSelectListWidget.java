
package com.yiming.wrutils.client.gui.widget.search.dropdown;

import net.minecraft.text.Text;

import java.util.ArrayList;

public class DropDownSingleSelectListWidget extends DropDownSelectListWidget {
    public DropDownSingleSelectListWidget(int x, int y, int width, int height, int headerHeight, int itemHeight, Text message, ArrayList<String> list) {
        super(x, y, width, height, headerHeight, itemHeight, message, list);
        this.itemListWidget.setSingleChecked(true);
        this.itemListWidget.setCheckedItems(false);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX > this.getRight() - this.headerHeight && mouseY < this.getY() + this.headerHeight) {
            switch (this.checkState) {
                case CHECKED:
                    this.checkState = CheckState.UNCHECKED;
                    this.itemListWidget.setCheckedItems(false);
                    break;
                case UNCHECKED:
                case INDETERMINATE:
                    break;
            }
            return;
        }

        this.setExpanded(!this.isExpanded);
    }
}