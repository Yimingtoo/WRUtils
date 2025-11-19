package com.yiming.wrutils.client.input;

import com.yiming.wrutils.client.gui.MainMenuScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBoardManagement {
    private static KeyBinding keyBinding;
    private static KeyBinding keyBinding2;

    public static void keyBoardEventInit() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.setting", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.wrutils.category"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                MinecraftClient client1 = MinecraftClient.getInstance();
                client1.setScreen(new MainMenuScreen(Text.of("WRUtils Settings")));
            }
        });

        keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.setting1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.wrutils.category1"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding2.wasPressed()) {

            }
        });
    }


}
