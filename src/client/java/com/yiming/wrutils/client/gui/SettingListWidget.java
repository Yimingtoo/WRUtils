package com.yiming.wrutils.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;

public class SettingListWidget extends ElementListWidget<SettingListWidget.SettingEntry> {

    private final SettingGui parent;

    int buttonWidth;
    int buttonHeight;
    int rowWidth;

    public SettingListWidget(MinecraftClient client, SettingGui parent) {
        super(client, parent.width, parent.height - 100, 64, (parent.height) / 4, 32);
        this.parent = parent;
//        buttonWidth = (parent.width - 30) / 4;
//        buttonHeight = (parent.width - 30) / 16;
        buttonWidth = 100;
        buttonHeight = 20;
        rowWidth = parent.width - 30;

//        super(client, 200, 200, 32, 150, 32);
        System.out.println(String.format("width:%d height:%d 32:%d height-64:%d 32:%d", width, parent.height, 32, parent.height - 64, 32));
        this.addEntry(new SettingEntry(Text.literal("设置项 1"),
                ButtonWidget.builder(Text.literal("按钮 3"), button -> {
                            System.out.println("你点击了按钮 3！");
                        })
                        .dimensions(0, 0, buttonWidth, buttonHeight)
                        .tooltip(Tooltip.of(Text.literal("按钮 3 的提示")))
                        .build()
        ));

        this.addEntry(new SettingEntry(Text.literal("设置项 2"),
                ButtonWidget.builder(Text.literal("按钮 4"), button -> {
                            System.out.println("你点击了按钮 4！");
                        })
                        .dimensions(0, 0, buttonWidth, buttonHeight)
                        .tooltip(Tooltip.of(Text.literal("按钮 4 的提示")))
                        .build()
        ));

    }


    @Override
    public int getRowWidth() {
        return rowWidth;
    }

//    @Override
//    protected int getScrollbarPositionX() {
//        return this.width / 2 + this.getRowWidth() / 2 + 4;
//    }

    public class SettingEntry extends ElementListWidget.Entry<SettingEntry> {
        private final Text text;
        private final ButtonWidget button0;

        public SettingEntry(Text text, ButtonWidget button0) {
            this.text = text;
            this.button0 = button0;
        }


        @Override
        public List<? extends Element> children() {
            return List.of(button0);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(button0);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
//            System.out.println("SettingEntry render 方法被调用");
            button0.setX(x + 5);
            button0.setY(y-buttonHeight/2);
            button0.render(context, mouseX, mouseY, tickDelta);
            context.drawText(parent.getTextRenderer(), text, button0.getX() + button0.getWidth()+5, y-parent.getTextRenderer().getWrappedLinesHeight(text, parent.getTextRenderer().getWidth(text))/2, 0xFFFFFFFF, false);

        }
    }
}
