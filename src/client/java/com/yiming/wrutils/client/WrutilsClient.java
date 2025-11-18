package com.yiming.wrutils.client;

import com.yiming.wrutils.client.gui.MainMenuScreen;
import com.yiming.wrutils.client.render.CustomRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class WrutilsClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    private static KeyBinding keyBinding2;

    public static boolean wasLeftPressedLastTick = false;
    public static boolean wasRightPressedLastTick = false;


    @Override
    public void onInitializeClient() {
        guiInitialize();
        CustomRender.renderCustomModelOut();
    }


    public void guiInitialize() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.setting", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.wrutils.category"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
//                spawnItem();
                MinecraftClient client1 = MinecraftClient.getInstance();
//                client1.setScreen(new SettingGui(Text.of("WRUtils Settings")));
                client1.setScreen(new MainMenuScreen(Text.of("WRUtils Settings")));


            }
        });

        keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.setting1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.wrutils.category1"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding2.wasPressed()) {
//                CustomRender.model = MinecraftClient.getInstance().getBakedModelManager().getModel(BlockModels.getModelId(Blocks.REPEATER.getDefaultState()));
//                CustomRender.renderCustomModelOut();

            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null) {

                long window = client.getWindow().getHandle();
                boolean isPressedNow = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

                if (isPressedNow && !wasLeftPressedLastTick) {
//                    System.out.println("左键按下一次！");
                    if (client.player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
                        System.out.println("wooden_sword");

                        if (client.crosshairTarget != null) {
                            System.out.println("client.crosshairTarget.getPos() " + ((BlockHitResult) client.crosshairTarget).getBlockPos());

//                            SelectBoxes.getCurrent().setPos2(((BlockHitResult) client.crosshairTarget).getBlockPos());

                        }
                    }
                }
                if (!isPressedNow && wasLeftPressedLastTick) {
//                    System.out.println("左键抬起一次！");
                }

                wasLeftPressedLastTick = isPressedNow;

                isPressedNow = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

                if (isPressedNow && !wasRightPressedLastTick) {
//                    System.out.println("右键按下一次！");


                    if (client.player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
                        System.out.println("wooden_sword");

                        if (client.crosshairTarget != null) {
                            System.out.println("client.crosshairTarget.getPos() " + ((BlockHitResult) client.crosshairTarget).getBlockPos());

//                            SelectBoxes.getCurrent().setPos1(((BlockHitResult) client.crosshairTarget).getBlockPos());

                        }
                    }

                }
                if (!isPressedNow && wasRightPressedLastTick) {
//                    System.out.println("右键抬起一次！");
                }

                wasRightPressedLastTick = isPressedNow;

            }
        });
    }

//    public void spawnItem() {
//
//        ModItemEntity modItemEntity = new ModItemEntity(Wrutils.MOD_ITEM_ENTITY_ENTITY_TYPE, MinecraftClient.getInstance().player.getWorld());
//
//        boolean isClient1 = MinecraftClient.getInstance().player.getServer() == null;
//
//        MinecraftClient.getInstance().player.getWorld().spawnEntity(modItemEntity);
//    }


}
