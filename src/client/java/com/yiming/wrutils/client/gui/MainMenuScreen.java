package com.yiming.wrutils.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MainMenuScreen extends Screen {

    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private int textHeight;
    private int textWidth;

    // 设置界面标题
    public MainMenuScreen(Text title) {
        super(title);
    }

    @Override
    public void init() {
        initWidgets();
        this.addDrawableChild(new TextWidget(0, 40, this.width, 9, this.title, this.textRenderer));

    }

    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.of("Back"), button -> {
            this.client.setScreen(null);
            this.client.mouse.lockCursor();
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));
        adder.add(this.createButton(Text.of("Area Management"), () -> new AreaManagementScreen(Text.of("Area Management"), this)));
//        adder.add(this.createButton(Text.of("Test2"), () -> new SettingGui(Text.of("Test2"), this)));

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier) {
        return ButtonWidget.builder(text, button -> this.client.setScreen((Screen) screenSupplier.get())).width(98).build();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

    }
}
