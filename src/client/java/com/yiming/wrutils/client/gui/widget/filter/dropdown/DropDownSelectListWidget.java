package com.yiming.wrutils.client.gui.widget.filter.dropdown;

import com.yiming.wrutils.client.gui.widget.filter.clickable.BaseClickableWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.SkipFilterItem;
import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;

public class DropDownSelectListWidget extends ExpandableClickableWidget {
    protected ItemListWidget itemListWidget;
    protected int headerHeight;
    protected int headerWidth;
    protected int itemHeight;
    protected int expandWidth;
//    protected boolean isExpanded = false;

    protected final int interval = 2;


    protected ArrayList<String> itemStringList;

    protected final MinecraftClient client = MinecraftClient.getInstance();

    protected CheckState checkState = CheckState.CHECKED;


    public DropDownSelectListWidget(int x, int y, int headerWidth, int headerHeight, int expandWidth, int itemHeight, Text message, ArrayList<? extends FilterType<?>> list) {
        this(x, y, headerWidth, headerHeight, expandWidth, itemHeight, message);
        this.itemListWidget = new ItemListWidget(this.client, headerWidth, x, y + headerHeight + this.interval, itemHeight);
        this.itemListWidget.setItemEntries(list);
        this.setItemListWidgetEnabled(false);
        this.itemListWidget.setCheckedItems(true);
        this.itemListWidget.setOnFocusedAction(() -> {
            int size = this.itemListWidget.getCheckedCount();
            if (size == 0) {
                this.setCheckState(CheckState.UNCHECKED);
            } else if (size < this.itemListWidget.children().size()) {
                this.setCheckState(CheckState.INDETERMINATE);
            } else {
                this.setCheckState(CheckState.CHECKED);
            }
        });
    }

    protected DropDownSelectListWidget(int x, int y, int headerWidth, int headerHeight, int expandWidth, int itemHeight, Text message) {
        super(x, y, headerWidth, headerHeight, message);
        this.headerHeight = headerHeight;
        this.headerWidth = headerWidth;
        this.expandWidth = expandWidth;
        this.itemHeight = itemHeight;
    }

    public void setItemListWidgetEnabled(boolean enabled) {
        this.itemListWidget.visible = enabled;
        this.itemListWidget.active = enabled;
        this.height = this.headerHeight + (enabled ? this.interval + this.itemListWidget.getHeight() : 0);
    }

    public ItemListWidget getItemListWidget() {
        return this.itemListWidget;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    public CheckState getCheckState() {
        return this.checkState;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        this.setItemListWidgetEnabled(expanded);
    }

    public ArrayList<FilterType<?>> getFilterItemList() {
        ArrayList<FilterType<?>> list = new ArrayList<>();
        if (this.getCheckState() == CheckState.CHECKED) {
            list.add(new SkipFilterItem());
        } else {
            if (this.checkState != CheckState.UNCHECKED) {
                for (ItemListWidget.Entry entry : this.itemListWidget.children()) {
                    if (entry instanceof ItemListWidget.ItemEntry itemEntry) {
                        if (itemEntry.getCheckState() == CheckState.CHECKED) {
                            list.add(itemEntry.getItem());
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.translate(0, 0, 1); // 背景层级
        this.renderLayer1(context, mouseX, mouseY, delta);

        matrixStack.pop();
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
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = this.itemListWidget.mouseReleased(mouseX, mouseY, button);
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = this.itemListWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
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
                    this.itemListWidget.setCheckedItems(false);
                    break;
                case UNCHECKED:
                case INDETERMINATE:
                    this.checkState = CheckState.CHECKED;
                    this.itemListWidget.setCheckedItems(true);
                    break;
            }
            return;
        }

        this.setExpanded(!this.isExpanded);
    }


}
