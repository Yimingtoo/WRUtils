package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.client.gui.widget.SelectedAreaListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

public class SubAreaEditScreen extends Screen {
    private final SelectedAreaListWidget.Entry entry;
    private final HashSet<String> nameSet;
    private final Screen parent;

    private CustomTextFieldWidget nameField;
    private ButtonWidget buttonConfirm;
    private ButtonWidget buttonCancel;

    public SubAreaEditScreen(Screen parent, SelectedAreaListWidget.Entry entry, Set<String> nameSet) {
        super(Text.of(""));
        this.parent = parent;
        this.entry = entry;
        this.nameSet = new HashSet<>(nameSet);
    }

    @Override
    public void init() {
        initWidgets();
        this.addDrawableChild(new TextWidget(0, 40, this.width, 9, this.title, this.textRenderer));
    }

    private void initWidgets() {
        int y = 0;

        Text text = Text.literal("Sub Area Edit").styled(style -> style.withBold(true));
        TextWidget title = addDrawableChild(new TextWidget(text, this.textRenderer));
        SimplePositioningWidget.setPos(title, 8, 0, this.textRenderer.getWidth(text), 20);
        y += 25;

        Text text1 = Text.of("Rename:");
        TextWidget textWidget = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget, 12, y, this.textRenderer.getWidth(text1), 20);

        this.nameField = this.addDrawableChild(new CustomTextFieldWidget(this.textRenderer, 200, 20, Text.of("Sub Area Name")));
        SimplePositioningWidget.setPos(this.nameField, 75, y, 200, 20);
        this.nameField.setText(((SelectedAreaListWidget.OneAreaEntry) entry).getSelectBox().getName());
        y += 25;

        this.buttonConfirm = this.addDrawableChild(ButtonWidget.builder(Text.of("Confirm"), button -> {
            String name = nameField.getText();
            if (entry instanceof SelectedAreaListWidget.OneAreaEntry entry1) {
                if (nameSet.contains(name) && !entry1.getSelectBox().getName().equals(name)) {
                    // 重复命名
                } else if (name.isEmpty()) {
                    // 输入为空

                } else if (!entry1.getSelectBox().getName().equals(name)) {
                    this.nameSet.remove(entry1.getSelectBox().getName());
                    entry1.getSelectBox().setName(name);
                    this.nameSet.add(name);
                }
            }

            this.client.setScreen(this.parent);
        }).width(100).build());
        this.buttonCancel = this.addDrawableChild(ButtonWidget.builder(Text.of("Cancel"), button -> {
            this.client.setScreen(this.parent);
        }).width(100).build());
        SimplePositioningWidget.setPos(this.buttonConfirm, 12, y, 100, 20);
        SimplePositioningWidget.setPos(this.buttonCancel, 120, y, 100, 20);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

}
