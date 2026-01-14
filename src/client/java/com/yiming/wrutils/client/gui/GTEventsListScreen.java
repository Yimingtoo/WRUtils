package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.widget.GameTickEventsListWidget;
import com.yiming.wrutils.client.gui.widget.filter.FilterWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GTEventsListScreen extends AbstractSetupScreen {
    private GameTickEventsListWidget gameTickEventsListWidget;
    private FilterWidget filterWidget;
    private ButtonWidget filterButton;
    private ButtonWidget clearEventsButton;
    private ButtonWidget resetButton;

    protected GTEventsListScreen(Screen parent) {
        super(Text.of("Game Tick Events"), parent, false);
    }

    private boolean handleMouseEvent(Function<Element, Boolean> handler) {
        boolean result;
        // 处理鼠标事件
        result = handler.apply(this.filterWidget);
        result = handler.apply(this.filterButton) || result; // result 在"||"后面表示先执行handler.apply
        result = handler.apply(this.clearEventsButton) || result;
        result = handler.apply(this.resetButton) || result;
        result = result || handler.apply(this.gameTickEventsListWidget); // result 在"||"前面面表示如果result为true则不执行handler.apply
        return result;
    }


    @Override
    protected void init() {
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
        int x1 = this.filterWidget.getTabsWidth() + 5;

        this.filterButton = ButtonWidget.builder(Text.of("Filter"), button -> this.filterButtonOnClick())
                .width(40).position(x1, y).build();
        this.addDrawableChild(this.filterButton);
        this.filterButton.setHeight(16);
        this.filterButtonOnClick();
        x1 += this.filterButton.getWidth() + 5;


        this.resetButton = ButtonWidget.builder(Text.of("Reset"), button -> {
            this.filterWidget.resetFilter();
            this.gameTickEventsListWidget.refreshEvents();
        }).width(40).position(x1, y).build();
        this.resetButton.setHeight(16);
        this.addDrawableChild(this.resetButton);
        x1 += this.resetButton.getWidth() + 5;

        this.clearEventsButton = ButtonWidget.builder(Text.of("Clear"), button -> {
            DataManagerClient.clearEvents();
            this.gameTickEventsListWidget.refreshEvents();
        }).width(40).position(this.width - 45, y).build();
        this.clearEventsButton.setHeight(16);
        this.addDrawableChild(this.clearEventsButton);

    }

    private void filterButtonOnClick() {
        System.out.println("Filter button clicked");
        ArrayList<ArrayList<FilterType<?>>> filterManager = this.filterWidget.getFilterItems();
//        ArrayList<BaseEvent> filterEventList = DataManager.eventRecorder;
        ArrayList<BaseEvent> filterEventList = DataManager.eventRecorder.stream()
                .filter(event -> filterManager.stream()
                        .allMatch(items -> items.stream()
                                .anyMatch(item -> item.collectOrNot(event))))
                .collect(Collectors.toCollection(ArrayList::new));

        this.gameTickEventsListWidget.setEvents(filterEventList);
        DataManagerClient.filterEventList = filterEventList;
        DataManagerClient.filterEventPointer = 0;
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
