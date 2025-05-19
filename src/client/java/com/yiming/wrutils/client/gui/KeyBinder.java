package com.yiming.wrutils.client.gui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Deprecated
public class KeyBinder {
    private  static KeyBinding keyBinding;
    public KeyBinder() {
//        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                "key.wrutils.setting",
//                InputUtil.Type.KEYSYM,
//                GLFW.GLFW_KEY_I,
//                "key.wrutils.category"));
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            while (keyBinding.wasPressed()) {
//                System.out.println("-----------------Key I was pressed sdf------------");
//                MinecraftClient.getInstance().setScreen(new SettingGui(Text.empty()));
//            }
//        });
    }
}
