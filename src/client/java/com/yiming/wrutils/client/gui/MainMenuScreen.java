package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.gui.malilib_gui.ConfigsScreen;
import com.yiming.wrutils.client.gui.widget.CustomButtonWidget;
import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MainMenuScreen extends Screen {

    private CustomButtonWidget customButtonWidget;

    // 设置界面标题
    public MainMenuScreen() {
        super(Text.of("WRUtils Settings"));
    }

    @Override
    public void init() {
        initWidgets();
        this.changeCustomButtonLevel();
        this.addDrawableChild(new TextWidget(0, 40, this.width, 9, this.title, this.textRenderer));
    }


    private void initWidgets() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(Text.of("Back"), button -> {
            client1.setScreen(null);
            client1.mouse.lockCursor();
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));

        adder.add(this.createButton(Text.of("Area Group"), () -> new AreaGroupScreen(this)));

        adder.add(ButtonWidget.builder(Text.of("Configs Menu"), button -> {
            GuiBase.openGui(new ConfigsScreen());
        }).width(100).build());

        this.customButtonWidget = adder.add(new CustomButtonWidget(0, 0, 204, 20, 3, Text.of("Test3")), 2);
        this.customButtonWidget.setOnClickAction(
                () -> client1.setScreen(new AreaGroupScreen(this)),
                () -> client1.setScreen(new AreaListScreen(this, Wrutils.getCurrentBoxes())),
                () -> client1.setScreen(new SubAreaScreen(this, Wrutils.getCurrentBoxes(), Wrutils.getCurrentSelectBox()))
        );
//        adder.add(ButtonWidget.builder(Text.of("Test"), button -> {
//            if (cnt > 3) cnt = 0;
//            customButtonWidget.setLevel(cnt++);
//        }).width(204).build(), 2);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private void changeCustomButtonLevel() {
        String text = "Area Group Setting";
        String text1 = "Area: null";
        String text2 = "SubArea: null";


        int i = 1;
        if (Wrutils.getCurrentBoxes() != null) {
            i++;
            text1 = "Area: " + Wrutils.getCurrentBoxes().getName();
        }

        if (Wrutils.getCurrentSelectBox() != null) {
            i++;
            text2 = "SubArea: " + Wrutils.getCurrentSelectBox().getName();
        }
        this.customButtonWidget.setLevel(i);
        this.customButtonWidget.setText(text, text1, text2);
    }

    private ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier) {
        return ButtonWidget.builder(text, button -> MinecraftClient.getInstance().setScreen(screenSupplier.get())).width(98).build();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

    }
}
