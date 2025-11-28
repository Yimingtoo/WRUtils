package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.AreaListWidget;
import com.yiming.wrutils.client.gui.widget.EventsListWidget;
import com.yiming.wrutils.client.gui.widget.GameTickEventsListWidget;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GTEventsListScreen extends AbstractSetupScreen {
    private GameTickEventsListWidget gameTickEventsListWidget;

    protected GTEventsListScreen(Screen parent) {
        super(Text.of("Game Tick Events"), parent, false);
    }

    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        super.init();
        int y = 26;
        this.gameTickEventsListWidget = new GameTickEventsListWidget(this, 0, 80, this.width, this.height - 64 - 30);
        this.gameTickEventsListWidget.setEvents(DataManager.eventRecorder);
        this.addDrawableChild(this.gameTickEventsListWidget);


//        super.init();
//        EventsListWidget eventsListWidget = new EventsListWidget(this, DataManager.currentGameTime, 0, 80, this.width, Text.of("Game Tick Events"));
//        eventsListWidget.addChild(DataManager.eventRecorder.get(0));
//        eventsListWidget.addChild(DataManager.eventRecorder.get(1));
//        eventsListWidget.addChild(DataManager.eventRecorder.get(2));
//        this.addDrawableChild(eventsListWidget);

    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

}
