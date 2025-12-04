package com.yiming.wrutils.client.gui.widget.search;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;

public class DropDownTextFieldListWidget extends BaseClickableWidget {
    private final ItemTextFieldListWidget itemTextFieldListWidget;
    private final int headerHeight;
    private final int headerWidth;
    private final int itemHeight;
    private final int expandWidth;
    private boolean isExpanded = false;

    private final AddRemoveButtonWidget addButton;

    private final int interval = 2;

    private CheckState checkState = CheckState.CHECKED;

    public DropDownTextFieldListWidget(int x, int y, int expandWidth, int headerWidth, int headerHeight, int itemHeight, Text message, ArrayList<String> list) {
        super(x, y, headerWidth, headerHeight, message);
        this.itemTextFieldListWidget = new ItemTextFieldListWidget(MinecraftClient.getInstance(), expandWidth, x, y + headerHeight + this.interval, itemHeight, this);
        this.itemTextFieldListWidget.setItemEntries(list);
        this.headerWidth = headerWidth;
        this.headerHeight = headerHeight;
        this.itemHeight = itemHeight;
        this.expandWidth = expandWidth;
        this.setItemListWidgetEnabled(false);

        this.addButton = new AddRemoveButtonWidget(0, 0, 14, headerHeight, true, () -> {
            System.out.println("add clicked");
            this.itemTextFieldListWidget.addItemEntry("New Item");
            this.itemTextFieldListWidget.updateParentWidgetCheckedState();
            this.setExpanded(true);
        });
    }

    public void setItemListWidgetEnabled(boolean enabled) {
        this.itemTextFieldListWidget.visible = enabled;
        this.itemTextFieldListWidget.active = enabled;
        this.height = this.headerHeight + (enabled ? this.itemTextFieldListWidget.getHeight() : 0);
        this.width = enabled ? this.expandWidth : this.headerWidth;
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
//        super.renderWidget(context, mouseX, mouseY, delta);
        int interval = this.isExpanded ? this.interval : 0;
        context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 5, this.getY() + 5, Colors.WHITE);
        context.fill(this.getX(), this.getY(), this.getX() + this.headerWidth, this.getY() + this.headerHeight + interval, WrutilsColor.GREY_0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.headerWidth - 1, this.getY() + this.headerHeight + interval - 1, WrutilsColor.BLACK);
        this.itemTextFieldListWidget.render(context, mouseX, mouseY, delta);

        context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 4, this.getY() + this.headerHeight / 2 - 4, this.getX() + this.headerWidth - this.headerHeight / 2 + 5, this.getY() + this.headerHeight / 2 + 5, Colors.WHITE);
        context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 3, this.getY() + this.headerHeight / 2 - 3, this.getX() + this.headerWidth - this.headerHeight / 2 + 4, this.getY() + this.headerHeight / 2 + 4, Colors.BLACK);
        if (this.checkState == CheckState.CHECKED) {
            context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2 - 2, this.getX() + this.headerWidth - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 3, Colors.GREEN);
        } else {
            if (this.checkState == CheckState.INDETERMINATE) {
                context.fill(this.getX() + this.headerWidth - this.headerHeight / 2 - 2, this.getY() + this.headerHeight / 2, this.getX() + this.headerWidth - this.headerHeight / 2 + 3, this.getY() + this.headerHeight / 2 + 1, Colors.GREEN);
            }
        }

        this.addButton.setPosition(this.getX() + this.headerWidth - this.headerHeight - this.headerHeight / 2,
                this.getY() + this.headerHeight / 2 - this.addButton.getHeight() / 2);
        this.addButton.render(context, mouseX, mouseY, delta);

        matrixStack.pop();

    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            this.setExpanded(false);
        }
        if (this.active) {
            if (this.addButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
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
    public boolean charTyped(char chr, int keyCode) {
        if (this.itemTextFieldListWidget.charTyped(chr, keyCode)) {
            return true;
        }
        return super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.itemTextFieldListWidget.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX > this.getX() + this.headerWidth - this.headerHeight
                && mouseX < this.getX() + this.headerWidth
                && mouseY < this.getY() + this.headerHeight
        ) {
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
