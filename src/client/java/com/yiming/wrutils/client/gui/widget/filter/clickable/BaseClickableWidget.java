package com.yiming.wrutils.client.gui.widget.filter.clickable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

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
