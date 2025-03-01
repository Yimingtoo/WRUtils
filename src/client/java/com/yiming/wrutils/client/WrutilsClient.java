package com.yiming.wrutils.client;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.block.entity.ModItemEntity;
import com.yiming.wrutils.client.gui.SettingGui;
import com.yiming.wrutils.client.render.ModItemEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import com.yiming.wrutils.block.entity.ModBlockEntityTypes;
import com.yiming.wrutils.client.render.CustomBlockEntityRenderer;

public class WrutilsClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        guiInitialize();
        EntityRendererRegistry.register(Wrutils.MOD_ITEM_ENTITY_ENTITY_TYPE, ModItemEntityRenderer::new);
    }

    public void guiInitialize() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.wrutils.setting", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.wrutils.category"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                spawnItem();
                MinecraftClient.getInstance().setScreen(new SettingGui(Text.of("WRUtils Settings")));
            }
        });
    }

    public void spawnItem() {
        ModItemEntity modItemEntity = new ModItemEntity(Wrutils.MOD_ITEM_ENTITY_ENTITY_TYPE, MinecraftClient.getInstance().player.getWorld());
    }


}
