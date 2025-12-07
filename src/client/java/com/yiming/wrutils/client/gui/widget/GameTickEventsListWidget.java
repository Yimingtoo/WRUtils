package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class GameTickEventsListWidget extends ScrollableWidget {
    private final Screen parent;
    private final ArrayList<EventsListWidget> eventsListWidgets = new ArrayList<>();
    private final TextRenderer textRenderer;

    public GameTickEventsListWidget(Screen parent, int x, int y, int width, int height) {
        super(x, y, width, height, Text.of(""));
        this.parent = parent;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

    }

    public void setEvents(ArrayList<BaseEvent> baseEvents) {
        if (baseEvents.isEmpty()) {
            this.eventsListWidgets.clear();
            return;
        }
        this.eventsListWidgets.clear();
        long gameTick = -1;
        EventsListWidget eventsListWidget = null;
        for (BaseEvent baseEvent : baseEvents) {
            if (baseEvent.getTimeStamp().gameTime() != gameTick) {
                gameTick = baseEvent.getTimeStamp().gameTime();
                eventsListWidget = new EventsListWidget(this.parent, gameTick, (int) (gameTick - DataManager.eventOriginTick), 0, 0, this.getWidth() - 10, Text.of("Tick: "));
                eventsListWidget.addChild(baseEvent);
                this.eventsListWidgets.add(eventsListWidget);
            } else {
                if (eventsListWidget != null) {
                    eventsListWidget.addChild(baseEvent);
                }
            }
        }
        this.refreshScroll();
    }


    protected int getContentsHeight() {
        int height = 20;
        for (EventsListWidget weight : this.eventsListWidgets) {
            height += weight.getHeight();
        }
        return height;
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
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF000000);
        context.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
        int y = 0;
        for (EventsListWidget widget : this.eventsListWidgets) {
            int widgetTop = this.getY() + y - (int) this.getScrollY();
            int widgetBottom = widgetTop + widget.getHeight();
            y += widget.getHeight();
            if (widgetBottom >= this.getY() && widgetTop <= this.getBottom()) {
                widget.setPosition(this.getX(), widgetTop);
                widget.render(context, mouseX, mouseY, delta);
            }
        }
        context.disableScissor();
        this.drawScrollbar(context);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            for (EventsListWidget weight : this.eventsListWidgets) {
                if (weight.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        boolean bl = this.checkScrollbarDragged(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button) || bl;
    }


    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.getMessage());
    }
}
