package com.yiming.wrutils.client.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yiming.wrutils.client.render.DrawStyle;
import com.yiming.wrutils.client.render.ZoneRenderer2;
import com.yiming.wrutils.client.render.ZoneRenderer3;
import com.yiming.wrutils.data.select_box.SelectBoxes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
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

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/client/render/Fog;)V",
                    shift = At.Shift.BEFORE))
    private void malilib_onRenderWorldPreWeather(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline,
                                                 Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {

        Vec3i pos1 = SelectBoxes.getCurrent().pos1();
        Vec3i pos2 = SelectBoxes.getCurrent().pos2();


        ZoneRenderer3.drawBoxFaces(positionMatrix, camera,
                pos1,
                pos2,
                new DrawStyle(DrawStyle.WHITE, 0.2f)
        );

    }
}
