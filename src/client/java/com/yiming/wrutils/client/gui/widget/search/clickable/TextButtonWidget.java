package com.yiming.wrutils.client.gui.widget.search.clickable;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class TextButtonWidget extends BaseClickableWidget {
    private TextWidget textWidget;

    public TextButtonWidget(int x, int y, int width, int height, Text message, Runnable onClickAction) {
        super(x, y, width, height, message);
        this.textWidget = new TextWidget(0, 0, width, height, this.getMessage(), this.textRenderer);
        this.onClickAction = onClickAction;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.isMouseOver(mouseX, mouseY) ? WrutilsColor.WHITE : WrutilsColor.GREY_0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, WrutilsColor.BLACK);

        this.textWidget.setPosition(this.getX(), this.getY());
        this.textWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
    }


}
