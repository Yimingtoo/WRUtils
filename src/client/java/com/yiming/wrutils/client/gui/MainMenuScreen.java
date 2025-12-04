package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.Notification;
import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.malilib_gui.ConfigsScreen;
import com.yiming.wrutils.client.gui.widget.CustomButtonWidget;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.DataManager;
import fi.dy.masa.malilib.gui.GuiBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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
        }).width(228).build(), 2, gridWidget.copyPositioner().marginTop(50));

        adder.add(ButtonWidget.builder(Text.of("Events"), button -> {
            GuiBase.openGui(new GTEventsListScreen(this));
//            GuiBase.openGui(new TestScreen(Text.of("Test"),this));
        }).width(110).build());

        adder.add(ButtonWidget.builder(Text.of("Configs Menu"), button -> {
            GuiBase.openGui(new ConfigsScreen());
        }).width(110).build());

        adder.add(ButtonWidget.builder(this.getRecordButtonText(), button -> {
            if (!DataManager.isSelectionEnabled) {
                // "请先开启选取!"
                return;
            }
            DataManager.isRecording = !DataManager.isRecording;
            button.setMessage(this.getRecordButtonText());
        }).width(110).build());

        adder.add(ButtonWidget.builder(this.getSelectionButtonText(), button -> {
            if (DataManager.isRecording) {
                // "请先停止记录!"
                return;
            }
            DataManager.isSelectionEnabled = !DataManager.isSelectionEnabled;
            button.setMessage(this.getSelectionButtonText());
            this.customButtonWidget.setEnabled(DataManager.isSelectionEnabled);
        }).width(110).build());


        this.customButtonWidget = adder.add(new CustomButtonWidget(0, 0, 228, 20, 3, Text.of("Test3")), 2);
        this.customButtonWidget.setOnClickAction(
                () -> client1.setScreen(new AreaGroupScreen(this)),
                () -> client1.setScreen(new AreaListScreen(this, DataManager.getCurrentBoxes())),
                () -> client1.setScreen(new SubAreaScreen(this, DataManager.getCurrentBoxes(), DataManager.getCurrentSelectBox()))
        );

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private Text getRecordButtonText() {
        boolean bl = !DataManager.isRecording;
        return Text.literal(bl ? "Start" : "Stop").styled(style -> style.withColor(bl ? 0xFFFFFFFF : 0xFFFFFF00))
                .append(Text.literal(" "))
                .append(Text.literal("Record").styled(style -> style.withColor(0xFFFFFFFF)));
    }

    private Text getSelectionButtonText() {
        boolean bl1 = DataManager.isSelectionEnabled;
        return Text.literal("Selection:").styled(style -> style.withColor(0xFFFFFFFF))
                .append(Text.literal(" "))
                .append(Text.literal(bl1 ? "Enabled" : "Disabled").styled(style -> style.withColor(bl1 ? 0xFF00FF00 : WrutilsColor.RED_0)));
    }

    private void changeCustomButtonLevel() {
        String text = "Area Group Setting";
        String text1 = "Area: null";
        String text2 = "SubArea: null";


        int i = 1;
        if (DataManager.getCurrentBoxes() != null) {
            i++;
            text1 = "Area: " + DataManager.getCurrentBoxes().getName();
        }

        if (DataManager.getCurrentSelectBox() != null) {
            i++;
            text2 = "SubArea: " + DataManager.getCurrentSelectBox().getName();
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
