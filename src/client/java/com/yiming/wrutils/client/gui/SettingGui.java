package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.SettingListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

@Deprecated
@Environment(EnvType.CLIENT)
public class SettingGui extends Screen {

    private SettingListWidget settingListWidget;
    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private int textHeight;
    private int textWidth;
    private final Screen parent;

    public SettingGui(Text title) {
        super(title);
        this.parent = null;
    }

    public SettingGui(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void init() {
        this.layout.addHeader(this.title, this.textRenderer);
        this.textWidth = this.textRenderer.getWidth(this.title);
        this.textHeight = this.textRenderer.fontHeight;
        this.settingListWidget = new SettingListWidget(MinecraftClient.getInstance(), this);
        addDrawableChild(settingListWidget);
    }

    public int getTextHeight() {
        return textHeight;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
//        context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        context.drawTextWithShadow(SettingGui.this.textRenderer, this.title, SettingGui.this.width / 2 - this.textWidth/ 2, this.textHeight+5, Colors.WHITE);
        settingListWidget.render(context, mouseX, mouseY, delta);
    }
    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

}
