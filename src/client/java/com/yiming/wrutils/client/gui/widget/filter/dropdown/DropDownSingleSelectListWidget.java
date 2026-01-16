
package com.yiming.wrutils.client.gui.widget.filter.dropdown;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.SingleSelectItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.AreaListItem;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.SubAreaItem;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterTypeTemp;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DropDownSingleSelectListWidget extends DropDownSelectListWidget {
    private final ItemListWidget subItemListWidget;
    private LinkedHashMap<String, ArrayList<String>> itemStringMap;
    private LinkedHashMap<FilterType<?>, ArrayList<? extends FilterType<?>>> itemMap = new LinkedHashMap<>();


    public DropDownSingleSelectListWidget(int x, int y, int headerWidth, int headerHeight, int expandWidth, int itemHeight, Text message, FilterTypeTemp areaFilter, FilterTypeTemp subAreaFilter, ArrayList<FilterType<?>> list) {
        super(x, y, headerWidth, headerHeight, expandWidth, itemHeight, message);

        this.itemListWidget = new SingleSelectItemListWidget(this.client, headerWidth, x, y + headerHeight + this.interval, itemHeight);
        this.subItemListWidget = new ItemListWidget(this.client, this.expandWidth - headerWidth, x + this.itemListWidget.getWidth(), y + headerHeight + this.interval, itemHeight);

        for (FilterType<?> entry : list) {
            if (entry instanceof AreaListItem areaListItem) {
                this.itemMap.put(areaListItem, SubAreaItem.getSubAreaItems(areaListItem.getValue().getList(), areaListItem.getBlockType()));
            }
        }


        this.setSubItemListWidgetEnabled(false);
        this.subItemListWidget.setOnFocusedAction(this::onFocused0);

        this.itemListWidget.setItemEntries(areaFilter);
        this.setItemListWidgetEnabled(false);
        this.itemListWidget.setOnFocusedAction(this::onFocused1);

    }

    private void onFocused0() {
        if (this.itemListWidget.getFocused() instanceof ItemListWidget.ItemEntry entry) {
//            int size = this.subItemListWidget.getCheckedCount();
//            if (size == 0) {
//                entry.setCheckState(CheckState.UNCHECKED);
//                this.setCheckState(CheckState.UNCHECKED);
//            } else if (size < this.subItemListWidget.children().size()) {
//                entry.setCheckState(CheckState.INDETERMINATE);
//                this.setCheckState(CheckState.INDETERMINATE);
//            } else {
//                entry.setCheckState(CheckState.CHECKED);
//                this.setCheckState(CheckState.CHECKED);
//            }
            CheckState checkState1 = this.subItemListWidget.getPatrentCheckState();
            entry.setCheckState(checkState1);
            this.setCheckState(checkState1);
        }
    }

    private void onFocused1() {
        if (this.itemListWidget.getFocused() != null) {
            if (this.itemListWidget.getFocused() instanceof ItemListWidget.ItemEntry entry) {
                if (!(entry instanceof ItemListWidget.HeaderItemEntry)) {
                    this.setCheckState(entry.getCheckState());
                    String entryName = entry.getItemName();
                    String masterString = this.subItemListWidget.getMasterString();
                    this.setSubItemListWidgetEnabled(true);
                    if (!entryName.equals(masterString)) {
//                        this.setSubItemListWidgetItems(entry.getItem());
                    } else {
                        if (entry.getCheckState() == CheckState.CHECKED) {
                            this.subItemListWidget.setSelectedCheckedItems(true);
                        } else if (entry.getCheckState() == CheckState.UNCHECKED) {
                            this.subItemListWidget.setSelectedCheckedItems(false);
                        }
                    }
                } else {
                    this.setCheckState(entry.getCheckState());
                    this.setSubItemListWidgetItems(null);
                    this.setSubItemListWidgetEnabled(false);
                }
            }
        } else {
            this.setSubItemListWidgetItems(null);
            this.setSubItemListWidgetEnabled(false);
        }
    }

    public void setSubItemListWidgetItems(@Nullable FilterType<?> key) {
//        if (key != null) {
//            this.subItemListWidget.setMasterString(key.getName());
//            this.subItemListWidget.setItemEntries(this.itemMap.get(key));
//        } else {
//            this.subItemListWidget.setMasterString(null);
//            this.subItemListWidget.setItemEntries(new ArrayList<>());
//        }
//        this.subItemListWidget.setSelectedCheckedItems(true);
//        this.subItemListWidget.setScrollY(0);
    }


    public void setSubItemListWidgetEnabled(boolean enabled) {
        this.subItemListWidget.visible = enabled;
        this.subItemListWidget.active = enabled;
    }


    @Override
    protected void renderLayer1(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderLayer1(context, mouseX, mouseY, delta);
        this.subItemListWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (!this.isMouseOver(mouseX, mouseY)) {
//            this.setExpanded(false);
//        }
        if (this.subItemListWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = this.subItemListWidget.mouseReleased(mouseX, mouseY, button);
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = this.subItemListWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.subItemListWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
//        if (this.isMouseOver(mouseX, mouseY)) {
//            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
//        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX > this.getX() + this.headerWidth - this.headerHeight
                && mouseX < this.getX() + this.headerWidth
                && mouseY < this.getY() + this.headerHeight) {
            switch (this.checkState) {
                case CHECKED:
                    this.checkState = CheckState.UNCHECKED;
                    this.itemListWidget.setSelectedCheckedItems(false);
                    this.subItemListWidget.setSelectedCheckedItems(false);
                    break;
                case UNCHECKED:
                case INDETERMINATE:
                    this.checkState = CheckState.CHECKED;
                    this.itemListWidget.setSelectedCheckedItems(true);
                    this.subItemListWidget.setSelectedCheckedItems(true);
                    break;
            }
            return;
        }

        this.setExpanded(!this.isExpanded);

    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!expanded) {
            this.setSubItemListWidgetEnabled(false);
        }
    }

    @Override
    public void setItemListWidgetEnabled(boolean enabled) {
        this.itemListWidget.visible = enabled;
        this.itemListWidget.active = enabled;
        this.height = this.headerHeight + (enabled ? this.interval + this.itemHeight * 6 + 10 : 0);
        this.width = enabled ? this.expandWidth : this.headerWidth;
    }
}