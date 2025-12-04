package com.yiming.wrutils.client.gui.widget.search;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class BaseClickableWidget extends ClickableWidget {
    protected final TextRenderer textRenderer;
    protected Runnable onClickAction;

    public BaseClickableWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public BaseClickableWidget(int x, int y, int width, int height, Text message, Runnable onClickAction) {
        this(x, y, width, height, message);
        this.onClickAction = onClickAction;
    }

    public void setVisibleAndActive(boolean flag) {
        this.visible = flag;
        this.active = flag;
    }


    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 5, this.getY() + 5, Colors.WHITE);
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), WrutilsColor.GREY_0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, WrutilsColor.BLACK);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.onClickAction != null) {
            this.onClickAction.run();
        }
    }


}
