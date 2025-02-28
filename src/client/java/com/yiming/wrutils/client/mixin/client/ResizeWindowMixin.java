package com.yiming.wrutils.client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(MinecraftClient.class)
public abstract class ResizeWindowMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {
    Boolean isResize = false;

    public ResizeWindowMixin(String string) {
        super(string);
    }
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/WindowProvider;createWindow(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/util/Window;"), index = 0)
    private WindowSettings mainMixin1(WindowSettings settings) {
        System.out.println("mainMixin1 " + settings.width + ": " + settings.height);
        return isResize ? new WindowSettings(400, 300, settings.fullscreenWidth, settings.fullscreenHeight, settings.fullscreen) : settings;
    }

}
