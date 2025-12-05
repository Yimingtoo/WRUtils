package com.yiming.wrutils.client.gui.widget.filter.clickable;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class AddRemoveButtonWidget extends BaseClickableWidget {
    private final boolean addOrRemove;

    public AddRemoveButtonWidget(int x, int y, int width, int height, boolean addOrRemove, Runnable onClickAction) {
        super(x, y, width, height, Text.of(""));
        this.addOrRemove = addOrRemove;
        this.onClickAction = onClickAction;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int length = 9;

        context.fill(this.getX() + this.getWidth() / 2 - length / 2,
                this.getY() + this.getHeight() / 2 - length / 2,
                this.getX() + this.getWidth() / 2 + length - length / 2,
                this.getY() + this.getHeight() / 2 + length - length / 2,
                0xFFC0C0C0);
        context.fill(this.getX() + this.getWidth() / 2 - length / 2 + 1,
                this.getY() + this.getHeight() / 2 - length / 2 + 1,
                this.getX() + this.getWidth() / 2 + length - length / 2 - 1,
                this.getY() + this.getHeight() / 2 + length - length / 2 - 1,
                Colors.BLACK);
        context.fill(
                this.getX() + this.getWidth() / 2 - length / 2 + 2,
                this.getY() + this.getHeight() / 2,
                this.getX() + this.getWidth() / 2 + length - length / 2 - 2,
                this.getY() + this.getHeight() / 2 + 1,
                this.addOrRemove ? Colors.GREEN : WrutilsColor.RED_0);
        if (this.addOrRemove) {
            context.fill(
                    this.getX() + this.getWidth() / 2,
                    this.getY() + this.getHeight() / 2 - length / 2 + 2,
                    this.getX() + this.getWidth() / 2 + 1,
                    this.getY() + this.getHeight() / 2 + length - length / 2 - 2,
                    Colors.GREEN);
        }

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
    }
}
