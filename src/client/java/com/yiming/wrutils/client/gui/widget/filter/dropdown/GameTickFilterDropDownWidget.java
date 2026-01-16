package com.yiming.wrutils.client.gui.widget.filter.dropdown;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.clickable.AddRemoveButtonWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.GameTickItemsWidget;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterTypeTemp;
import com.yiming.wrutils.client.gui.widget.filter.items.GameTickFilter;
import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class GameTickFilterDropDownWidget extends ExpandableClickableWidget {
    private final GameTickItemsWidget gameTickItemsWidget;
    private final int headerHeight;
    private final int headerWidth;
    private final int itemHeight;
    private final int expandWidth;
    private final GameTickFilter filter;

    private final AddRemoveButtonWidget addButton;

    private final int interval = 2;

    private CheckState checkState = CheckState.CHECKED;

    public GameTickFilterDropDownWidget(int x, int y, int headerWidth, int headerHeight, int expandWidth, int itemHeight, Text message, GameTickFilter filter) {
        super(x, y, headerWidth, headerHeight, message);
        this.gameTickItemsWidget = new GameTickItemsWidget(MinecraftClient.getInstance(), expandWidth, x, y + headerHeight + this.interval, itemHeight, this, filter);
        this.gameTickItemsWidget.setItemEntries(filter);
        this.filter = filter;
        this.headerWidth = headerWidth;
        this.headerHeight = headerHeight;
        this.itemHeight = itemHeight;
        this.expandWidth = expandWidth;
        this.setItemListWidgetEnabled(false);
        this.setCheckState(this.gameTickItemsWidget.getPatrentCheckState());

        this.addButton = new AddRemoveButtonWidget(0, 0, 14, headerHeight, true, () -> {
            // todo : extract to extend class
            this.gameTickItemsWidget.addItemEntry(new GameTickFilter.Item(0, CheckState.CHECKED));
            this.setCheckState(this.gameTickItemsWidget.getPatrentCheckState());
            this.setExpanded(true);
        });
    }

    public void setItemListWidgetEnabled(boolean enabled) {
        this.gameTickItemsWidget.visible = enabled;
        this.gameTickItemsWidget.active = enabled;
        this.height = this.headerHeight + (enabled ? this.gameTickItemsWidget.getHeight() : 0);
        this.width = enabled ? this.expandWidth : this.headerWidth;
    }

    public GameTickItemsWidget getItemTextFieldListWidget() {
        return this.gameTickItemsWidget;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        this.setItemListWidgetEnabled(expanded);
    }

    public CheckState getCheckState() {
        return this.checkState;
    }

    public FilterTypeTemp getFilter() {
        return filter;
    }


    @Override
    public void reset() {
        this.setCheckState(CheckState.CHECKED);
        this.gameTickItemsWidget.reset();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.translate(0, 0, 1); // 背景层级
        // 绘制背景
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

        this.gameTickItemsWidget.render(context, mouseX, mouseY, delta);

        this.addButton.setPosition(this.getX() + this.headerWidth - this.headerHeight - this.headerHeight / 2,
                this.getY() + this.headerHeight / 2 - this.addButton.getHeight() / 2);
        this.addButton.render(context, mouseX, mouseY, delta);

        matrixStack.pop();

    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = mouseX < this.getX() + this.headerWidth && mouseY < this.getY() + this.headerHeight;
        boolean bl2 = mouseY > this.getY() + this.headerHeight;
        boolean bl3 = bl || bl2;
        if (!(this.isMouseOver(mouseX, mouseY) && bl3)) {
            if (this.gameTickItemsWidget.isTextFieldFocused()) {
                this.gameTickItemsWidget.removeTextFieldFocused();
                return true;
            } else {
                this.setExpanded(false);
            }
        }
        if (this.active) {
            if (this.addButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        if (this.gameTickItemsWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        return bl3 && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = this.gameTickItemsWidget.mouseReleased(mouseX, mouseY, button);
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = this.gameTickItemsWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.gameTickItemsWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        if (this.isMouseOver(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return false;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if (this.gameTickItemsWidget.charTyped(chr, keyCode)) {
            return true;
        }
        return super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.gameTickItemsWidget.keyPressed(keyCode, scanCode, modifiers)) {
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
                    this.gameTickItemsWidget.setCheckedItems(CheckState.UNCHECKED);
                    break;
                case UNCHECKED:
                case INDETERMINATE:
                    this.checkState = CheckState.CHECKED;
                    this.gameTickItemsWidget.setCheckedItems(CheckState.CHECKED);
                    break;
            }
            return;
        }

//        this.setExpanded(!this.isExpanded);
        if (this.isExpanded) {
            if (!this.gameTickItemsWidget.isTextFieldFocused()) {
                this.setExpanded(false);
            }
        } else {
            this.setExpanded(true);
        }
    }


}
