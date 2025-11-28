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
    private final int relativeTick;
    private final ArrayList<BaseWeight> childWeights = new ArrayList<>();
    private final Screen parent;
    private final TextRenderer textRenderer;
    private boolean isExpanded;


    public EventsListWidget(Screen parent, long gameTick, int relativeTick, int x, int y, int width, Text header) {
        super(x, y, width, 20, header);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.parent = parent;
        this.gameTick = gameTick;
        this.relativeTick = relativeTick;
        this.setMessage(Text.of(header.getString() + relativeTick));
        this.isExpanded = false;
    }

    public void addChild(BaseEvent event) {
        this.childWeights.add(this.createWeight(event));
        this.updateWeights();
    }

    public void updateWeights() {
        this.height = 20;
        for (BaseWeight weight : this.childWeights) {
            if (this.isExpanded) {
                this.height += weight.getHeight();
            }
            weight.visible = this.isExpanded;
        }
    }

    public BaseWeight createWeight(BaseEvent event) {
        BlockState state = event.getTargetBlockInfo().state();
        if (event instanceof ScheduledTickAddEvent) {
            return new ScheduledTickAddEventWeight(this.getX(), this.getY(), this.getWidth() - 13, Text.of(state == null ? "null" : state.getBlock().getName().getString()), event);
        } else {
            // TODO: 需要修改
            return new NeighborChangedEventWeight(this.getX(), this.getY(), this.getWidth() - 13, Text.of(state == null ? "null" : state.getBlock().getName().getString()), event);

        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWithShadow(this.textRenderer, this.getMessage(), 5, this.getY() + 5, Colors.WHITE);
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFA0A0A0);
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF000000);

        if (this.isExpanded) {
            int y = 20;
            for (BaseWeight weight : this.childWeights) {
                weight.setPosition(this.getX() + 10, this.getY() + y);
                weight.render(context, mouseX, mouseY, delta);
                y += weight.getHeight();
            }
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (BaseWeight weight : this.childWeights) {
            if (weight.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
//        System.out.println(this.getMessage().getString() + " clicked");
        if (mouseY > this.getY() && mouseY < this.getY() + 20) {
            this.isExpanded = !this.isExpanded;
        }
        updateWeights();
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

        @Override
        public void onClick(double mouseX, double mouseY) {
//            System.out.println(this.getMessage().getString() + " " + event.getTimeStamp().eventId() + " clicked");
        }
    }

    public static class ScheduledTickAddEventWeight extends BaseWeight {
        public ScheduledTickAddEventWeight(int x, int y, int width, Text message, BaseEvent event) {
            super(x, y, width, 30, message, event);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.event.toString(), this.getX() + 5, this.getY() + this.height / 2 - 9 / 2, Colors.WHITE);
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFA0A0A0);
            context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF000000);
        }
    }

    public static class NeighborChangedEventWeight extends BaseWeight {
        public NeighborChangedEventWeight(int x, int y, int width, Text message, BaseEvent event) {
            super(x, y, width, 20, message, event);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.event.toString(), this.getX() + 5, this.getY() + this.height / 2 - 9 / 2, Colors.WHITE);
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFA0A0A0);
            context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, 0xFF000000);
        }
    }

}
