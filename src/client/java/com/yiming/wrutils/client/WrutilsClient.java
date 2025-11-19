package com.yiming.wrutils.client;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.gui.MainMenuScreen;
import com.yiming.wrutils.client.input.KeyBoardManagement;
import com.yiming.wrutils.client.input.MouseClickManagement;
import com.yiming.wrutils.client.render.CustomRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class WrutilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        guiInitialize();
        CustomRender.renderCustomModelOut();
    }

    public void guiInitialize() {
        KeyBoardManagement.keyBoardEventInit();
        ClientTickEvents.END_CLIENT_TICK.register(MouseClickManagement::clickEvent);
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
