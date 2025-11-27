package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;

public class EventsListWidget extends ClickableWidget {
    private final long gameTick;
    private boolean isChildrenVisible;
    private final ArrayList<BaseWeight> childWeights = new ArrayList<>();
    private final Screen parent;
    private final TextWidget textWidget;
    private final TextRenderer textRenderer;


    public EventsListWidget(Screen parent, long gameTick, int x, int y, int width, Text message) {
        super(x, y, width, 20, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.parent = parent;
        this.gameTick = gameTick;
        this.textWidget = new TextWidget(0, 0, width, 20, Text.of("Game Tick: " + this.gameTick), textRenderer);

    }

    public void addChild(BaseEvent event) {
        this.childWeights.add(this.createWeight(event));
        this.updateWeights();
    }

    public void updateWeights() {
        this.height = 20 + this.childWeights.size() * 20;

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWithShadow(this.textRenderer, this.getMessage(), 5, this.getY() + 5, Colors.WHITE);
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFA0A0A0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF000000);


        int index = 1;
        for (BaseWeight weight : this.childWeights) {
            weight.setPosition(this.getX(), this.getY() + 20 * index++);
            weight.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    public BaseWeight createWeight(BaseEvent event) {
        BlockState state = event.getTargetBlockInfo().state();
        if (event instanceof ScheduledTickAddEvent) {
            return new ScheduledTickAddEventWeight(this.getX(), this.getY(), this.getWidth() , Text.of(state == null ? "null" : state.getBlock().getName().getString()), event);
        } else {
            // TODO: 需要修改
            return new ScheduledTickAddEventWeight(this.getX(), this.getY(), this.getWidth() , Text.of(state == null ? "null" : state.getBlock().getName().getString()), event);

        }
    }

    public abstract static class BaseWeight extends ClickableWidget {
        protected final BaseEvent event;

        public BaseWeight(int x, int y, int width, int height, Text message, BaseEvent event) {
            super(x, y, width, height, message);
            this.event = event;
        }

        public BaseEvent getEvent() {
            return event;
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

    public static class ScheduledTickAddEventWeight extends BaseWeight {
        public ScheduledTickAddEventWeight(int x, int y, int width, Text message, BaseEvent event) {
            super(x, y, width, 20, message, event);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.event.toString(), 5, this.getY() + this.height / 2 - 9 / 2, Colors.WHITE);
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFA0A0A0);
            context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF000000);
        }
    }

}
