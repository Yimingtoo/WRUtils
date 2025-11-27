package com.yiming.wrutils.client.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;

public class GameTickEventsListWidget extends ScrollableWidget {
    private final Screen parent;
//    private final ArrayList<Entry> entries = new ArrayList<>();

    public GameTickEventsListWidget(Screen parent, int x, int y, int width, int height) {
        super(x, y, width, height, Text.of(""));
        this.parent = parent;
    }


    protected int getContentsHeight() {
//        return this.wrapped.getHeight();
        // TODO: 设置ContentsHeight
        return 0;
    }

    @Override
    protected int getContentsHeightWithPadding() {
        int padding = 8;
        return this.getContentsHeight() + padding;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9.0;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getMessage());
    }
}
