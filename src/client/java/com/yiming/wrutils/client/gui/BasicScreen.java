package com.yiming.wrutils.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;

public class BasicScreen extends Screen {
    public final MinecraftClient minecraftClient = MinecraftClient.getInstance();


    public BasicScreen() {
        super(ScreenTexts.EMPTY);
        this.client = minecraftClient;
    }
}
