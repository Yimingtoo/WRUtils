package com.yiming.wrutils.client.utils;


public class WrutilsColor {
    public static final int RED = 0xFFFF0000;
    public static final int GREEN = 0xFF00FF00;
    public static final int BLUE = 0xFF0000FF;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int PURPLE = 0xFF800080;
    public static final int LIGHT_BLUE = 0xFF81C6F0;
    public static final int PINK = 0xFFE269A2;

    public static final int BLACK = 0xFF000000;
    public static final int GREY_0 = 0xFFC0C0C0;
    public static final int GREY_1 = 0xFF202020;
    public static final int RED_0 = 0xFFFF6060;

    public int color; // 默认红色不透明
    public float alpha;
    public float red;
    public float green;
    public float blue;

    public WrutilsColor() {
        this.color = 0xFFFF0000;
    }

    public WrutilsColor(int color) {
        this.color = color;
        this.alpha = ((color >> 24) & 0xFF) / 255.0f;
        this.red = ((color >> 16) & 0xFF) / 255.0f;
        this.green = ((color >> 8) & 0xFF) / 255.0f;
        this.blue = (color & 0xFF) / 255.0f;
    }

    public WrutilsColor(int color, float alpha) {
        this.red = ((color >> 16) & 0xFF) / 255.0f;
        this.green = ((color >> 8) & 0xFF) / 255.0f;
        this.blue = (color & 0xFF) / 255.0f;
        this.alpha = alpha;
        this.color = color & 0xFFFFFF | ((int) (alpha * 255)) << 24;
    }

    public int getColor() {
        return this.color;
    }

    public static WrutilsColor getMixedStyle(WrutilsColor style1, WrutilsColor style2) {
        int color1 = style1.color;
        int color2 = style2.color;
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (a1 + a2) / 2;
        int r = (r1 + r2) / 2;
        int g = (g1 + g2) / 2;
        int b = (b1 + b2) / 2;
        return new WrutilsColor((a << 24) | (r << 16) | (g << 8) | b);
    }

}
