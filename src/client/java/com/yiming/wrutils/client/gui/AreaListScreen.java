package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.AreaGroupWidget;
import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.client.gui.widget.AreaListWidget;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class AreaListScreen extends Screen {
    private boolean initialized;
    private AreaListWidget areaListWidget;
    private CustomTextFieldWidget AreaNameField;
    private AreaGroupWidget.Entry entry;
    private Set<String> nameSet;

    private Screen parent;

    @Deprecated
    protected AreaListScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    public AreaListScreen(Screen parent, AreaGroupWidget.Entry entry, Set<String> nameSet) {
        super(Text.of(""));
        this.parent = parent;
        this.entry = entry;
        this.nameSet = new HashSet<>(nameSet);

    }

    public void setSelectBoxesName() {
        if (entry instanceof AreaGroupWidget.OneGroupEntry entry1) {
            String name = AreaNameField.getText();
            String oldName = entry1.getSelectBoxes().getName();
            if (nameSet.contains(name) && !oldName.equals(name)) {
                // 重复命名
            } else if (name.isEmpty()) {
                // 输入为空

            } else if (!oldName.equals(name)) {
                this.nameSet.remove(oldName);
                entry1.getSelectBoxes().setName(name);
                this.nameSet.add(name);
            }
        }
    }

    private SelectBoxes getSelectBoxes() {
        return ((AreaGroupWidget.OneGroupEntry) this.entry).getSelectBoxes();
    }

    @Override
    protected void init() {
        int y = 0;
        if (this.initialized) {
            this.areaListWidget.setDimensionsAndPosition(this.width, this.height - 64 - 50, 0, 80);
        } else {
            this.initialized = true;
            this.areaListWidget = new AreaListWidget(this, this.client, this.width, this.height - 64 - 30, 80, 24);

        }
//        SelectBoxes boxes = ((AreaGroupWidget.OneGroupEntry) this.entry).getSelectBoxes();
        this.areaListWidget.setAreaEntries(this.getSelectBoxes());
//        this.selectedAreaListWidget.setSelected(boxes.getCurrentBox());
        this.addDrawableChild(this.areaListWidget);

        Text text = Text.literal("Selected Area Setting").styled(style -> style.withBold(true));
        TextWidget title = addDrawableChild(new TextWidget(text, this.textRenderer));
        SimplePositioningWidget.setPos(title, 8, 0, this.textRenderer.getWidth(text), 20);

        y += 25;
        Text text1 = Text.of("Area name:");
        TextWidget textWidget = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget, 12, y, this.textRenderer.getWidth(text1), 20);

        this.AreaNameField = this.addDrawableChild(new CustomTextFieldWidget(this.textRenderer, 200, 20, Text.of("Area Name")));
        this.AreaNameField.setLostFocusAction(() -> {
//            this.areaListWidget.getSelectBoxes().setName(this.AreaNameField.getText());
            setSelectBoxesName();
        });
        SimplePositioningWidget.setPos(this.AreaNameField, 75, y, 200, 20);
        this.AreaNameField.setText(this.getSelectBoxes().getName());

        y += 25;
        Text text2 = Text.of("Sub Areas:");
        TextWidget textWidget2 = addDrawableChild(new TextWidget(text2, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget2, 12, y, this.textRenderer.getWidth(text2), 20);
        ButtonWidget createSubArea = this.addDrawableChild(ButtonWidget.builder(Text.of("Create Sub Area"), button -> {
            SelectBox selectBox = new SelectBox(this.client.player.getBlockPos(), this.client.player.getBlockPos());
            this.getSelectBoxes().addAndSetCurrent(selectBox);
            areaListWidget.appendAreaEntry(selectBox);
        }).width(100).build());
        SimplePositioningWidget.setPos(createSubArea, 75, y, 100, 20);


    }


    @Override
    public void close() {
        this.setSelectBoxesName();
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void refreshWidgetPositions() {
//        super.refreshWidgetPositions();
        this.setSelectBoxesName();
        this.client.setScreen(new AreaListScreen(this.parent, this.entry, this.nameSet));

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
