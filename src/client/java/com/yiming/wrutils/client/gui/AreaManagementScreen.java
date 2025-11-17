package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.SelectedAreaListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class AreaManagementScreen extends Screen {
    private boolean initialized;
    private SelectedAreaListWidget selectedAreaListWidget;
    private ButtonWidget buttonOpen;
    private ButtonWidget buttonClear;
    private Screen parent;

    protected AreaManagementScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }


    @Override
    protected void init() {
        if (this.initialized) {
            this.selectedAreaListWidget.setDimensionsAndPosition(this.width, this.height - 64 - 100, 0, 120);
        } else {
            this.initialized = true;
            this.selectedAreaListWidget = new SelectedAreaListWidget(this.client, this.width, this.height - 64 - 100, 120, 24);
        }
        this.addDrawableChild(this.selectedAreaListWidget);

        this.buttonClear = this.addDrawableChild(ButtonWidget.builder(Text.of("Clear"), button -> {
            this.selectedAreaListWidget.clearAreaEntries();
        }).width(100).build());
        this.buttonOpen = this.addDrawableChild(ButtonWidget.builder(Text.of("Open"), button -> {
            this.selectedAreaListWidget.setAreaEntries(MinecraftClient.getInstance());
        }).width(100).build());
        DirectionalLayoutWidget directionalLayoutWidget = DirectionalLayoutWidget.vertical();
        AxisGridWidget axisGridWidget = directionalLayoutWidget.add(new AxisGridWidget(308, 20, AxisGridWidget.DisplayAxis.HORIZONTAL));
        axisGridWidget.add(this.buttonClear);
        axisGridWidget.add(this.buttonOpen);
        directionalLayoutWidget.refreshPositions();
        directionalLayoutWidget.add(EmptyWidget.ofHeight(4));
        SimplePositioningWidget.setPos(directionalLayoutWidget, 0, this.height - 64, this.width, 64);

    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void refreshWidgetPositions() {
//        super.refreshWidgetPositions();
        this.client.setScreen(new AreaManagementScreen(Text.of("Area Management"), this.parent));
    }


}
