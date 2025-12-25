package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.malilib_gui.ConfigsScreen;
import com.yiming.wrutils.client.gui.widget.CustomButtonWidget;
import com.yiming.wrutils.client.utils.WrutilsClientUtils;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
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
    private final MinecraftClient client1;
    private ButtonWidget playButton;
    private ButtonWidget eventButton;
    private CustomButtonWidget customButtonWidget;
    private ButtonWidget selectionButton;
    private ButtonWidget recordButton;

    // 设置界面标题
    public MainMenuScreen() {
        super(Text.of("WRUtils Settings"));
        this.client1 = MinecraftClient.getInstance();
    }

    @Override
    public void init() {
        initWidgets();
        this.changeCustomButtonLevel();
        this.addDrawableChild(new TextWidget(0, 40, this.width, 9, this.title, this.textRenderer));
    }


    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        // 返回按钮
        adder.add(ButtonWidget.builder(Text.of("Back"), button -> {
            this.client1.setScreen(null);
            this.client1.mouse.lockCursor();
        }).width(228).build(), 2, gridWidget.copyPositioner().marginTop(50));

        // 事件按钮
        AxisGridWidget axisGridWidget = adder.add(new AxisGridWidget(110, 20, AxisGridWidget.DisplayAxis.HORIZONTAL));
        this.eventButton = axisGridWidget.add(ButtonWidget.builder(Text.of("Events"), button -> {
            this.client1.setScreen(new GTEventsListScreen(this));
            this.updatePlayEventButton();
        }).width(110).build());
        this.playButton = axisGridWidget.add(ButtonWidget.builder(Text.of(DataManagerClient.isFilterEventRender ? "Stop" : "Play"), button -> {
            DataManagerClient.isFilterEventRender = !DataManagerClient.isFilterEventRender;
            button.setMessage(Text.of(DataManagerClient.isFilterEventRender ? "Stop" : "Play"));
        }).width(38).build());
        this.updatePlayEventButton();

        // 配置界面按钮
        adder.add(ButtonWidget.builder(Text.of("Configs Menu"), button -> {
            this.client1.setScreen(new ConfigsScreen(this));
        }).width(110).build());

        // 选取按钮
        this.selectionButton = adder.add(ButtonWidget.builder(this.getSelectionButtonText(), button -> {
            if (DataManager.isRecording) {
                this.recordButton.onPress();
            }

            // 如果没有添加SubArea，自动添加SubArea
            if (!DataManager.isSelectionEnabled) {
                SelectBoxes boxes = DataManager.getCurrentBoxes();
                MinecraftClient client1 = MinecraftClient.getInstance();
                if (!(client1.player == null) && boxes != null && boxes.getList().isEmpty()) {
                    SelectBox selectBox = new SelectBox(client1.player.getBlockPos(),
                            client1.player.getBlockPos(), WrutilsClientUtils.getPlayerDimension());
                    boxes.addAndSetCurrent(selectBox);
                }
            }
            this.changeCustomButtonLevel();

            DataManager.isSelectionEnabled = !DataManager.isSelectionEnabled;
            button.setMessage(this.getSelectionButtonText());
            this.customButtonWidget.setEnabled(DataManager.isSelectionEnabled);
        }).width(110).build());

        // 记录按钮
        this.recordButton = adder.add(ButtonWidget.builder(this.getRecordButtonText(), button -> {
            if (!DataManager.isSelectionEnabled) {
                this.selectionButton.onPress();
            }
            DataManager.isRecording = !DataManager.isRecording;
            button.setMessage(this.getRecordButtonText());
        }).width(110).build());

        // 区域按钮
        this.customButtonWidget = adder.add(new CustomButtonWidget(0, 0, 228, 20, 3, Text.of("")), 2);
        this.customButtonWidget.setOnClickAction(
                () -> this.client1.setScreen(new AreaGroupScreen(this)),
                () -> this.client1.setScreen(new AreaListScreen(this, DataManager.getCurrentBoxes())),
                () -> this.client1.setScreen(new SubAreaScreen(this, DataManager.getCurrentBoxes(), DataManager.getCurrentSelectBox()))
        );


//        adder.add(ButtonWidget.builder(Text.of(DataManagerClient.isFilterEventRender ? "Stop" : "Play"), button -> {
//            DataManagerClient.isFilterEventRender = !DataManagerClient.isFilterEventRender;
//            button.setMessage(Text.of(DataManagerClient.isFilterEventRender ? "Stop" : "Play"));
//        }).width(110).build());

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    public void updatePlayEventButton() {
        boolean isEmpty = DataManagerClient.filterEventList.isEmpty();
        playButton.visible = !isEmpty;
        eventButton.setWidth(isEmpty ? 110 : 68);
    }

    private Text getRecordButtonText() {
        boolean bl = !DataManager.isRecording;
        return Text.literal(bl ? "Start Record" : "Recording").styled(style -> style.withColor(bl ? 0xFFFFFFFF : 0xFFFFFF00));

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
