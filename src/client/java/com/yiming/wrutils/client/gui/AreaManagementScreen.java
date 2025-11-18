package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.gui.widget.SelectedAreaListWidget;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class AreaManagementScreen extends Screen {
    private boolean initialized;
    private SelectedAreaListWidget selectedAreaListWidget;
    private ButtonWidget buttonOpen;
    private ButtonWidget buttonClear;
    private TextFieldWidget AreaNameField;
    ButtonWidget setAreaName;
    ButtonWidget cancelAreaName;
    private Screen parent;

    protected AreaManagementScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }


    @Override
    protected void init() {
        int y = 0;
        if (this.initialized) {
            this.selectedAreaListWidget.setDimensionsAndPosition(this.width, this.height - 64 - 50, 0, 80);
        } else {
            this.initialized = true;
            this.selectedAreaListWidget = new SelectedAreaListWidget(this.client, this.width, this.height - 64 - 50, 80, 24);

        }
        SelectBoxes boxes = Wrutils.selectedAreaManagement.getCurrentBoxes();
        this.selectedAreaListWidget.setAreaEntries(boxes);
        this.selectedAreaListWidget.setSelected(boxes.getCurrentBox());
        this.addDrawableChild(this.selectedAreaListWidget);

        Text text = Text.literal("Selected Area Setting").styled(style -> style.withBold(true));
        TextWidget title = addDrawableChild(new TextWidget(text, this.textRenderer));
        SimplePositioningWidget.setPos(title, 8, 0, this.textRenderer.getWidth(text), 20);

        y += 25;
        Text text1 = Text.of("Area name:");
        TextWidget textWidget = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget, 12, y, this.textRenderer.getWidth(text1), 20);

        this.AreaNameField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, 200, 20, Text.of("Area Name")) {
            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == GLFW.GLFW_KEY_ENTER) {
                    ((TextFieldWidget) this).setFocused(false);

                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }

            @Override
            public void setFocused(boolean focused) {
                super.setFocused(focused);
                if (!focused) {
                    this.setText("changed");
                }
            }

        });

        SimplePositioningWidget.setPos(this.AreaNameField, 75, y, 200, 20);
        this.AreaNameField.setText("Area Name");

        y += 25;
        Text text2 = Text.of("Sub Areas:");
        TextWidget textWidget2 = addDrawableChild(new TextWidget(text2, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget2, 12, y, this.textRenderer.getWidth(text1), 20);
        ButtonWidget createSubArea = this.addDrawableChild(ButtonWidget.builder(Text.of("Create Sub Area"), button -> {
            SelectBox selectBox = new SelectBox(this.client.player.getBlockPos(), this.client.player.getBlockPos());
            Wrutils.selectedAreaManagement.getCurrentBoxes().addAndSetCurrent(selectBox);
            selectedAreaListWidget.appendAreaEntry(selectBox);
        }).width(100).build());
        SimplePositioningWidget.setPos(createSubArea, 75, y, 100, 20);

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
//        directionalLayoutWidget.add(EmptyWidget.ofHeight(4));
        SimplePositioningWidget.setPos(directionalLayoutWidget, 0, this.height - 48, this.width, 64);

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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);
        if (!handled) {
            Element element = getFocused();
            if (element != null) {
                element.setFocused(false);
            }
        }
        return true;
    }


}
