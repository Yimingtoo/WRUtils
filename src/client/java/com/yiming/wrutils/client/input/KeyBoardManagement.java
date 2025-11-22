package com.yiming.wrutils.client.input;

import com.yiming.wrutils.client.gui.MainMenuScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBoardManagement {
    private static KeyBinding keyBindingI;
    private static KeyBinding keyBindingLeftAlt;

    public static void keyBoardEventInit() {
        keyBindingI = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.key_i", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.wrutils.category"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingI.wasPressed()) {
                MinecraftClient client1 = MinecraftClient.getInstance();
                client1.setScreen(new MainMenuScreen());
                System.out.println("key.wrutils.key_i");

            }
        });

        keyBindingLeftAlt = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.key_left_alt", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.wrutils.category1"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingLeftAlt.wasPressed()) {

                System.out.println("key.wrutils.setting1");
            }
        });
    }


}
