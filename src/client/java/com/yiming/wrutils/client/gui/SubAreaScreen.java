package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.HashSet;

public class SubAreaScreen extends AbstractSetupScreen {
    private final SelectBoxes upperSelectBoxes;
    private final SelectBox selfSelectBox;

    private CustomTextFieldWidget nameField;

    public SubAreaScreen(Screen parent, SelectBoxes upperSelectBoxes, SelectBox selfSelectBox) {
        super(Text.of("Sub Area Setting"), parent);
        this.parent = parent;
        this.upperSelectBoxes = upperSelectBoxes;
        this.selfSelectBox = selfSelectBox;
    }

    private boolean rename(String name) {
//        String name = nameField.getText();
        HashSet<String> nameSet = this.upperSelectBoxes.getSelectBoxListNames();
        if (nameSet.contains(name) && !this.selfSelectBox.getName().equals(name)) {
            // 重复命名
            System.out.println("Repeat Name");
        } else if (name.isEmpty()) {
            // 输入为空
            System.out.println("Empty Name");
        } else {
            this.selfSelectBox.setName(name);
            return true;
        }
        return false;
    }

    @Override
    protected void upperScreen() {
//        Screen screen = this.parent instanceof AreaListScreen ? this.parent : new AreaListScreen(this, upperSelectBoxes);

        Screen screen = this.parent instanceof AreaListScreen ? this.parent : new AreaListScreen(this.parent, upperSelectBoxes);
        MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public void init() {
        super.init();
        initWidgets();
    }

    private void initWidgets() {
        MinecraftClient client1 = MinecraftClient.getInstance();

        int y = 26;

        Text text1 = Text.of("Rename:");
        TextWidget textWidget = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget, 12, y, this.textRenderer.getWidth(text1), 20);

        this.nameField = this.addDrawableChild(new CustomTextFieldWidget(this.textRenderer, 200, 20, Text.of("Sub Area Name")));
        SimplePositioningWidget.setPos(this.nameField, 75, y, 200, 20);
        this.nameField.setText(this.selfSelectBox.getName());
        this.nameField.setLostFocusFunction(this::rename);
        y += 25;

        ButtonWidget buttonConfirm = this.addDrawableChild(ButtonWidget.builder(Text.of("Confirm"), button -> {
            if (rename(this.nameField.getText())) {
                client1.setScreen(this.parent);
            } else {
                System.out.println("rename fail");
            }
        }).width(100).build());
        ButtonWidget buttonCancel = this.addDrawableChild(ButtonWidget.builder(Text.of("Cancel"), button -> {
            client1.setScreen(this.parent);
        }).width(100).build());
        SimplePositioningWidget.setPos(buttonConfirm, 12, y, 100, 20);
        SimplePositioningWidget.setPos(buttonCancel, 120, y, 100, 20);
    }


    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

}
