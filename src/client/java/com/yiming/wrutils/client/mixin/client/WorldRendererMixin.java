package com.yiming.wrutils.client.mixin.client;

import com.yiming.wrutils.client.render.DrawColor;
import com.yiming.wrutils.client.render.ZoneRenderer3;
import com.yiming.wrutils.data.select_box.SelectBoxes;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "setupTerrain", at = @At("TAIL"))
    private void setupTerrain1(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {

    }

}
