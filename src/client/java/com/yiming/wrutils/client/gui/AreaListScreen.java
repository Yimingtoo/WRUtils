package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.utils.WrutilsClientUtils;
import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.client.gui.widget.AreaListWidget;
import com.yiming.wrutils.data.DataManager;
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

import java.util.HashSet;

@Environment(EnvType.CLIENT)
public class AreaListScreen extends AbstractSetupScreen {
    private boolean initialized;
    private AreaListWidget areaListWidget;
    private CustomTextFieldWidget AreaNameField;
    private final SelectBoxes selfSelectBoxes;

    public AreaListScreen(Screen parent, SelectBoxes selfSelectBoxes) {
        super(Text.of("Selected Area Setting"), parent);
        this.selfSelectBoxes = selfSelectBoxes;
    }

    public void setSelectBoxesName() {
        String name = AreaNameField.getText();
        String oldName = this.selfSelectBoxes.getName();
        HashSet<String> nameSet = DataManager.areaGroupManagement.getSelectBoxesListNames();
        if (nameSet.contains(name) && !oldName.equals(name)) {
            // 重复命名
        } else if (name.isEmpty()) {
            // 输入为空
        } else if (!oldName.equals(name)) {
            this.selfSelectBoxes.setName(name);
        }
    }


    @Override
    protected void upperScreen() {
        Screen screen = this.parent instanceof AreaGroupScreen ? this.parent : new AreaGroupScreen(this.parent);
        MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        super.init();
        int y = 26;
        if (this.initialized) {
            this.areaListWidget.setDimensionsAndPosition(this.width, this.height - 64 - 50, 0, 80);
        } else {
            this.initialized = true;
            this.areaListWidget = new AreaListWidget(this, this.client, this.width, this.height - 64 - 30, 80, 24);

        }
        this.areaListWidget.setAreaEntries(this.selfSelectBoxes);
        this.addDrawableChild(this.areaListWidget);


        Text text1 = Text.of("Area name:");
        TextWidget textWidget = addDrawableChild(new TextWidget(text1, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget, 12, y, this.textRenderer.getWidth(text1), 20);

        this.AreaNameField = this.addDrawableChild(new CustomTextFieldWidget(this.textRenderer, 200, 20, Text.of("Area Name")));
        this.AreaNameField.setLostFocusAction(() -> {
            setSelectBoxesName();
        });
        SimplePositioningWidget.setPos(this.AreaNameField, 75, y, 200, 20);
        this.AreaNameField.setText(this.selfSelectBoxes.getName());

        y += 25;
        Text text2 = Text.of("Sub Areas:");
        TextWidget textWidget2 = addDrawableChild(new TextWidget(text2, this.textRenderer));
        SimplePositioningWidget.setPos(textWidget2, 12, y, this.textRenderer.getWidth(text2), 20);
        ButtonWidget createSubArea = this.addDrawableChild(ButtonWidget.builder(Text.of("Create Sub Area"), button -> {
            if (client1.player == null) {
                return;
            }

            SelectBox selectBox = new SelectBox(client1.player.getBlockPos(), client1.player.getBlockPos(), WrutilsClientUtils.getPlayerDimension());
            this.selfSelectBoxes.addAndSetCurrent(selectBox);
            areaListWidget.appendAreaEntry(selectBox);
        }).width(100).build());
        SimplePositioningWidget.setPos(createSubArea, 75, y, 100, 20);

        ButtonWidget unselectButton = this.addDrawableChild(ButtonWidget.builder(Text.of("Unselect"), button -> {
            this.areaListWidget.setSelected((AreaListWidget.Entry) null);
        }).width(100).build());
        SimplePositioningWidget.setPos(unselectButton, 180, y, 100, 20);


    }


    @Override
    public void close() {
        this.setSelectBoxesName();
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void refreshWidgetPositions() {
//        super.refreshWidgetPositions();
        this.setSelectBoxesName();
        MinecraftClient.getInstance().setScreen(new AreaListScreen(this.parent, this.selfSelectBoxes));

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
