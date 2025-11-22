package com.yiming.wrutils.client.mixin.client;

import com.yiming.wrutils.client.WrutilsClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = 1000)
public class MinecraftClientMixin {
    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    public void doItemPick(CallbackInfo ci) {
        if (MinecraftClient.getInstance().player.getMainHandStack().getItem() == WrutilsClient.debugItem) {
            ci.cancel();
        }
    }
}
