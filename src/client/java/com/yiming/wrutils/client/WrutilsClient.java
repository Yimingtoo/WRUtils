package com.yiming.wrutils.client;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.input.KeyBoardManagement;
import com.yiming.wrutils.client.input.KeyCallbacks;
import com.yiming.wrutils.client.input.MouseManagement;
import com.yiming.wrutils.client.render.CustomRender;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class WrutilsClient implements ClientModInitializer {
    public static Item debugItem = Items.WOODEN_SWORD;

    @Override
    public void onInitializeClient() {
        guiInit();
        dataManagerInit();
        CustomRender.renderCustomModelOut();
        CustomRender.renderEvent();

        InputEventHandler.getKeybindManager().registerKeybindProvider(KeyBoardManagement.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(KeyBoardManagement.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(MouseManagement.getInstance());

        KeyCallbacks.keyCallBackInit(MinecraftClient.getInstance());


    }

    public void guiInit() {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerAfter(
                    IdentifiedLayer.MISC_OVERLAYS,
                    Identifier.of("mymod", "notifications"),
                    (context, tickCounter) -> {
                        Notification.renderNotification(context);
                    }
            );
        });
    }

    private void dataManagerInit() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {

        });
    }
}
