package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.gui.widget.AreaGroupWidget;
import com.yiming.wrutils.client.gui.widget.AreaListWidget;
import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class AreaGroupScreen extends Screen {
    private Screen parent;
    private boolean initialized;
    private AreaGroupWidget areaGroupWidget;

    protected AreaGroupScreen(Screen parent) {
        super(Text.of(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = 0;
        if (this.initialized) {
            this.areaGroupWidget.setDimensionsAndPosition(this.width, this.height - 64, 0, 50);
        } else {
            this.initialized = true;
            this.areaGroupWidget = new AreaGroupWidget(this, this.client, this.width, this.height - 64, 50, 30);

        }
        this.areaGroupWidget.setAreaEntries(Wrutils.areaGroupManagement);
        this.addDrawableChild(this.areaGroupWidget);

        Text text = Text.literal("Area Group").styled(style -> style.withBold(true));
        TextWidget title = addDrawableChild(new TextWidget(text, this.textRenderer));
        SimplePositioningWidget.setPos(title, 8, 0, this.textRenderer.getWidth(text), 20);

        y += 25;
        Text text1 = Text.of("Areas:");
        TextWidget textWidget2 = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget2, 12, y, this.textRenderer.getWidth(text1), 20);
        ButtonWidget createAreas = this.addDrawableChild(ButtonWidget.builder(Text.of("Create Area"), button -> {
            SelectBoxes selectBoxes = new SelectBoxes();
            Wrutils.areaGroupManagement.addAndSetCurrent(selectBoxes);
            areaGroupWidget.appendAreaEntry(selectBoxes);
        }).width(100).build());
        SimplePositioningWidget.setPos(createAreas, 75, y, 100, 20);


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
        this.client.setScreen(new AreaGroupScreen(this.parent));

    }


}
