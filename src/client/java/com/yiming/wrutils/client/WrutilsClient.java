package com.yiming.wrutils.client;

import com.yiming.wrutils.client.input.KeyBoardManagement;
import com.yiming.wrutils.client.input.KeyCallbacks;
import com.yiming.wrutils.client.input.MouseManagement;
import com.yiming.wrutils.client.render.CustomRender;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class WrutilsClient implements ClientModInitializer {
    public static Item debugItem = Items.WOODEN_SWORD;

    @Override
    public void onInitializeClient() {
        guiInitialize();
        CustomRender.renderCustomModelOut();

        InputEventHandler.getKeybindManager().registerKeybindProvider(KeyBoardManagement.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(KeyBoardManagement.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(MouseManagement.getInstance());

        KeyCallbacks.keyCallBackInit(MinecraftClient.getInstance());

    }

    public void guiInitialize() {
//        KeyBoardManagement.keyBoardEventInit();
//        ClientTickEvents.END_CLIENT_TICK.register(MouseClickManagement::clickEvent);
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerAfter(
                    IdentifiedLayer.MISC_OVERLAYS,
                    Identifier.of("mymod", "notifications"),
                    (context, tickCounter) -> {
                        // context = DrawContext，提供绘制方法
                        // tickCounter = RenderTickCounter，提供时间信息
                        Notification.renderNotification(context);
                    }
            );
        });


    }


}
