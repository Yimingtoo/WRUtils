package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.GameTickEventsListWidget;
import com.yiming.wrutils.client.gui.widget.filter.clickable.BaseClickableWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemTextFieldListWidget;
import com.yiming.wrutils.client.gui.widget.filter.FilterWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GTEventsListScreen extends AbstractSetupScreen {
    private GameTickEventsListWidget gameTickEventsListWidget;
    private FilterWidget filterWidget;
    private BaseClickableWidget filterButton;

    protected GTEventsListScreen(Screen parent) {
        super(Text.of("Game Tick Events"), parent, false);
    }

    private boolean handleMouseEvent(Function<Element, Boolean> handler) {
        boolean result = false;
        if (handler.apply(this.filterWidget)) result = true;
        if (handler.apply(this.filterButton)) result = true;
        if (!result && handler.apply(this.gameTickEventsListWidget)) result = true;
        return result;
    }


    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        super.init();
        int y = 26;

        int height1 = this.height - 50;
        int y1 = 45;
        this.gameTickEventsListWidget = new GameTickEventsListWidget(this, 0, y1, this.width, height1);
        this.gameTickEventsListWidget.setEvents(DataManager.eventRecorder);
        this.addDrawableChild(this.gameTickEventsListWidget);

        this.filterWidget = new FilterWidget(0, y, this.width, this.height - y - 10, Text.of("Filter"));
        this.filterWidget.setOnOpenStateChangeAction(() -> {
            if (this.filterWidget.isOpen()) {
                this.gameTickEventsListWidget.setHeight(height1 - 30);
                this.gameTickEventsListWidget.setY(y1 + 30);
            } else {
                this.gameTickEventsListWidget.setHeight(height1);
                this.gameTickEventsListWidget.setY(y1);
            }
        });
        this.addDrawableChild(this.filterWidget);

        int x1 = 288;
        this.filterButton = new BaseClickableWidget(this.filterWidget.getX() + x1, this.filterWidget.getY(), 18, 18, Text.of("F"), this::filterButtonOnClick) {
            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 6, this.getY() + 5, WrutilsColor.WHITE);
                context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), WrutilsColor.GREY_0);
                context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, WrutilsColor.BLACK);
            }
        };
        this.addDrawableChild(this.filterButton);

        this.filterButtonOnClick();
    }

    private void filterButtonOnClick() {
        System.out.println("Filter button clicked");

        ArrayList<FilterType> items = this.filterWidget.getFilterItems();
        ArrayList<BaseEvent> list = DataManager.eventRecorder.stream()
                .filter(event -> items.stream().anyMatch(item -> item.collectOrNot(event)))
                .collect(Collectors.toCollection(ArrayList::new));

        this.gameTickEventsListWidget.setEvents(list);

    }


    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseClicked(mouseX, mouseY, button));
        return bl || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseReleased(mouseX, mouseY, button));
        return bl || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = handleMouseEvent(widget -> widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        return bl || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean bl = handleMouseEvent(widget -> widget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        return bl || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        boolean bl = handleMouseEvent(widget -> widget.charTyped(chr, keyCode));
        return bl || super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = handleMouseEvent(widget -> widget.keyPressed(keyCode, scanCode, modifiers));
        return bl || super.keyPressed(keyCode, scanCode, modifiers);
    }

}
