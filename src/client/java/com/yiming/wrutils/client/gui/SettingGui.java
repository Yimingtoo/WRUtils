package com.yiming.wrutils.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class SettingGui extends Screen{
    public SettingGui(Text title) {
        super(title);
    }
    private SettingListWidget settingListWidget;
    public ButtonWidget button1;
    public ButtonWidget button2;
    @Override
    public void init() {
        button1 = ButtonWidget.builder(Text.literal("按钮 1"), button -> {
                    System.out.println("你点击了按钮 1！");
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("按钮 1 的提示")))
                .build();
        button2 = ButtonWidget.builder(Text.literal("按钮 2"), button -> {
                    System.out.println("你点击了按钮 2！");
                    MinecraftClient.getInstance().setScreen(null);
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("按钮 2 的提示")))
                .build();

        addDrawableChild(button1);
        addDrawableChild(button2);
        this.settingListWidget = new SettingListWidget(MinecraftClient.getInstance(), this);
        addDrawableChild(settingListWidget);
    }

//    @Override
//    public TextRenderer getTextRenderer() {
//        return super.getTextRenderer();
//    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        settingListWidget.render(context, mouseX, mouseY, delta);
    }
}
