
package com.yiming.wrutils.client.gui.widget.filter.dropdown;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.FilterManager;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.SingleSelectItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.AreaListItem;
import com.yiming.wrutils.client.gui.widget.filter.items.AreaListFilter;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterTypeTemp;
import com.yiming.wrutils.client.gui.widget.filter.items.SubAreaFilter;
import com.yiming.wrutils.client.gui.widget.filter.items.base.AnyItem;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

public class AreaSelectWidget extends ExpandableClickableWidget {
    protected SingleSelectItemListWidget itemListWidget;
    protected int headerHeight;
    protected int headerWidth;
    protected int itemHeight;
    protected int expandWidth;

    protected final int interval = 2;
    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected CheckState checkState = CheckState.CHECKED;

    private final ItemListWidget subItemListWidget;
    private final AreaListFilter areaFilter;
    private final SubAreaFilter subAreaFilter;

    public AreaSelectWidget(int x, int y, int headerWidth, int headerHeight, int expandWidth, int itemHeight, Text message, AreaListFilter areaFilter, SubAreaFilter subAreaFilter) {
        super(x, y, headerWidth, headerHeight, message);
        this.headerHeight = headerHeight;
        this.headerWidth = headerWidth;
        this.expandWidth = expandWidth;
        this.itemHeight = itemHeight;

        this.itemListWidget = new SingleSelectItemListWidget(this.client, headerWidth, x, y + headerHeight + this.interval, itemHeight);
        this.subItemListWidget = new ItemListWidget(this.client, this.expandWidth - headerWidth, x + this.itemListWidget.getWidth(), y + headerHeight + this.interval, itemHeight);

        this.itemListWidget.setItemEntries(areaFilter);
        this.setItemListWidgetEnabled(false);
        this.itemListWidget.setOnEntryClicked(this::onItemFocused);

        this.setSubItemListWidgetEnabled(false);
        this.subItemListWidget.setOnFocusedAction(this::onSubItemFocused);
        this.subItemListWidget.setItemEntries(subAreaFilter);

        this.areaFilter = areaFilter;
        this.subAreaFilter = subAreaFilter;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    private void onSubItemFocused() {
        if (this.itemListWidget.getFocused() instanceof ItemListWidget.ItemEntry entry) {
            CheckState checkState1 = this.subItemListWidget.getPatrentCheckState();
            entry.setCheckState(checkState1);
            this.setCheckState(checkState1);
        }
    }

    private void onItemFocused(ItemListWidget.ItemEntry entry, boolean entryChanged) {
        if (entry != null) {
            if (!(entry.getItem() instanceof AnyItem)) {
                this.setCheckState(entry.getCheckState());
                this.setSubItemListWidgetEnabled(true);
                if (this.getItemListWidget().getSelectedEntry() != entry) {
                    if (entry.getItem() instanceof AreaListFilter.Item item) {
                        this.setSubItemListWidgetItems(item);
                    } else {
                        this.setSubItemListWidgetItems(null);
                    }
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
            }
        } else {
            this.setSubItemListWidgetItems(null);
        }
    }

    public void setSubItemListWidgetItems(@Nullable AreaListFilter.Item item) {
        this.subAreaFilter.clear();
        if (item != null) {
            for (SelectBox selectBox : item.getSelectBoxes().getList()) {
                this.subAreaFilter.addItem(new SubAreaFilter.Item(this.subAreaFilter.getBlockType(), selectBox));
            }
        } else {
            this.setSubItemListWidgetEnabled(false);
        }
        this.subItemListWidget.setItemEntries(this.subAreaFilter);
        this.subItemListWidget.setSelectedCheckedItems(true);
        this.subItemListWidget.setScrollY(0);
    }


    public void setSubItemListWidgetEnabled(boolean enabled) {
        this.subItemListWidget.visible = enabled;
        this.subItemListWidget.active = enabled;
    }

    public SingleSelectItemListWidget getItemListWidget() {
        return (SingleSelectItemListWidget) this.itemListWidget;
    }


    protected void renderLayer1(DrawContext context, int mouseX, int mouseY, float delta) {
        int interval = this.isExpanded ? this.interval : 0;
        context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 5, this.getY() + 5, Colors.WHITE);
        context.fill(this.getX(), this.getY(), this.getX() + this.headerWidth, this.getY() + this.headerHeight + interval, WrutilsColor.GREY_0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.headerWidth - 1, this.getY() + this.headerHeight + interval - 1, WrutilsColor.BLACK);
        context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 4, this.getY() + this.headerHeight / 2 - 4, this.getX() + this.headerWidth - this.headerHeight / 2 + 5, this.getY() + this.headerHeight / 2 + 5, Colors.WHITE);
        context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 3, this.getY() + this.headerHeight / 2 - 3, this.getX() + this.headerWidth - this.headerHeight / 2 + 4, this.getY() + this.headerHeight / 2 + 4, Colors.BLACK);
        if (this.checkState == CheckState.CHECKED) {
            context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2 - 2, this.getX() + this.headerWidth - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 3, Colors.GREEN);
        } else {
            if (this.checkState == CheckState.INDETERMINATE) {
                context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2, this.getX() + this.headerWidth - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 1, Colors.GREEN);
            }
        }

        this.itemListWidget.render(context, mouseX, mouseY, delta);
        this.subItemListWidget.render(context, mouseX, mouseY, delta);
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        this.setItemListWidgetEnabled(expanded);
        if (!expanded) {
            this.setSubItemListWidgetEnabled(false);
        }
    }

    public void setItemListWidgetEnabled(boolean enabled) {
        this.itemListWidget.visible = enabled;
        this.itemListWidget.active = enabled;
        this.height = this.headerHeight + (enabled ? this.interval + this.itemHeight * 6 + 10 : 0);
        this.width = enabled ? this.expandWidth : this.headerWidth;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (!this.isMouseOver(mouseX, mouseY)) {
//            this.setExpanded(false);
//        }
        if (this.subItemListWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        boolean bl = mouseX < this.getX() + this.headerWidth && mouseY < this.getY() + this.headerHeight;
        boolean bl2 = mouseY > this.getY() + this.headerHeight;
        boolean bl3 = bl || bl2;
        if (!(this.isMouseOver(mouseX, mouseY) && bl3)) {
            this.setExpanded(false);
        }
        if (this.itemListWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return bl3 && super.mouseClicked(mouseX, mouseY, button);

//        return super.mouseClicked(mouseX, mouseY, button);
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

        if (this.itemListWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        if (this.isMouseOver(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return false;
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
    public void reset() {
        this.setCheckState(CheckState.CHECKED);
        this.itemListWidget.reset();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.translate(0, 0, 1); // 背景层级
        this.renderLayer1(context, mouseX, mouseY, delta);

        matrixStack.pop();
    }


}