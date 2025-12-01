package com.yiming.wrutils.client.gui.widget.search;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;

public class DropDownTextFieldListWidget extends BaseClickableWidget {
    private final ItemTextFieldListWidget itemTextFieldListWidget;
    private final int headerHeight;
    private final int totalHeight;
    private final int itemHeight;
    private boolean isExpanded = false;

    private CheckState checkState = CheckState.CHECKED;

    public DropDownTextFieldListWidget(int x, int y, int width, int height, int headerHeight, int itemHeight, Text message, ArrayList<String> list) {
        super(x, y, width, headerHeight, message);
        this.itemTextFieldListWidget = new ItemTextFieldListWidget(MinecraftClient.getInstance(), width, height - headerHeight, x, y + headerHeight, itemHeight, this);
        this.itemTextFieldListWidget.setItemEntries(list);
        this.headerHeight = headerHeight;
        this.totalHeight = height;
        this.itemHeight = itemHeight;
        this.setItemListWidgetEnabled(false);
    }

    public void setItemListWidgetEnabled(boolean enabled) {
        this.itemTextFieldListWidget.visible = enabled;
        this.itemTextFieldListWidget.active = enabled;
        this.height = enabled ? this.totalHeight : this.headerHeight;
    }

    public ItemTextFieldListWidget getItemTextFieldListWidget() {
        return this.itemTextFieldListWidget;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        this.setItemListWidgetEnabled(expanded);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.translate(0, 0, 1); // 背景层级
        // 绘制背景
        super.renderWidget(context, mouseX, mouseY, delta);
        this.itemTextFieldListWidget.render(context, mouseX, mouseY, delta);

        context.fill(this.getRight() - this.headerHeight / 2 - 4, this.getY() + this.headerHeight / 2 - 4, this.getRight() - this.headerHeight / 2 + 5, this.getY() + this.headerHeight / 2 + 5, Colors.WHITE);
        context.fill(this.getRight() - this.headerHeight / 2 - 3, this.getY() + this.headerHeight / 2 - 3, this.getRight() - this.headerHeight / 2 + 4, this.getY() + this.headerHeight / 2 + 4, Colors.BLACK);
        if (this.checkState == CheckState.CHECKED) {
            context.fill(this.getRight() - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2 - 2, this.getRight() - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 3, Colors.GREEN);
        } else if (this.checkState == CheckState.INDETERMINATE) {
            context.fill(this.getRight() - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2, this.getRight() - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 1, Colors.GREEN);
        }

        matrixStack.pop();

    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            this.setExpanded(false);
        }
        if (this.itemTextFieldListWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = this.itemTextFieldListWidget.mouseReleased(mouseX, mouseY, button);
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = this.itemTextFieldListWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.itemTextFieldListWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        if (this.isMouseOver(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX > this.getRight() - this.headerHeight && mouseY < this.getY() + this.headerHeight) {
            switch (this.checkState) {
                case CHECKED:
                    this.checkState = CheckState.UNCHECKED;
                    this.itemTextFieldListWidget.setCheckedItems(false);
                    break;
                case UNCHECKED:
                case INDETERMINATE:
                    this.checkState = CheckState.CHECKED;
                    this.itemTextFieldListWidget.setCheckedItems(true);
                    break;
            }
            return;
        }

        this.setExpanded(!this.isExpanded);
    }


}
