package com.yiming.wrutils.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notification {
    private static int posCenterX = 0;
    private static int posCenterY = -20;
    public static int cnt = 0;
    private static final List<Notification> notifications = new ArrayList<>();

    public final String message;
    public long startTime;
    public long duration;

    private static int getCenterPosX() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
    }

    private static int getCenterPosY() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
    }

    public Notification(String message, long duration) {
        this.message = message;
        this.duration = duration;
        this.startTime = Util.getMeasuringTimeMs();
    }

    public static void addNotification(String message, long duration_ms) {
        notifications.add(new Notification(message, duration_ms));
    }

    public static void renderNotification(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int y = getCenterPosY() + posCenterY; // 距离屏幕顶部的偏移
        int x = getCenterPosX() + posCenterX; // 屏幕右上角偏移

        Iterator<Notification> it = notifications.iterator();
        while (it.hasNext()) {
            Notification notification = it.next();
            context.drawText(client.textRenderer, notification.message, x - client.textRenderer.getWidth(notification.message) / 2, y, 0xFFFFFFFF, true);
            y += 12; // 每条通知向下偏移一点

            if (Util.getMeasuringTimeMs() - notification.startTime > notification.duration) {
                it.remove();
            }

        }
    }
}
