package com.yiming.wrutils.client.hud;

import com.yiming.wrutils.client.utils.WrutilsClientUtils;
import com.yiming.wrutils.client.utils.WrutilsColor;
import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class InfoHud {
    protected final MinecraftClient client;
    protected boolean isEnabled;
    protected ArrayList<Message> messages = new ArrayList<>();
    protected double textScale = 1;

    public InfoHud() {
        this.client = MinecraftClient.getInstance();
    }

    public boolean shouldRenderHub() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public void render(DrawContext context) {
        if (this.client.player == null) {
            return;
        }
        if (!WrutilsClientUtils.isPlayerHoldingRequiredItem()) {
            return;
        }
        if (!isEnabled) {
            return;
        }

        this.messages.forEach(message -> RenderUtils.renderText(
                message.x, message.y, message.scale * this.textScale, message.color, WrutilsColor.BLACK,
                HudAlignment.BOTTOM_LEFT, true, true,
                List.of(message.text), context));
    }

    protected static class Message {
        public String text;
        public int x;
        public int y;
        public int color;
        public double scale = 1;

        public Message(String text, int x, int y, int color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }


}
