package com.yiming.wrutils.client.render.deprecated;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
@Deprecated
public class TestRender {

    public TestRender() {
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MatrixStack matrices = context.matrixStack();
            VertexConsumerProvider vertexConsumers = context.consumers();
            Camera camera = context.camera();

            // 设置渲染位置（世界坐标）
            BlockPos renderPos = new BlockPos(1, 2, 3); // 你想显示的位置
            Vec3d relativePos = Vec3d.of(renderPos).subtract(camera.getPos());

            matrices.push();
            matrices.translate(relativePos.x, relativePos.y, relativePos.z);

            // 渲染一个平面贴图（例如侦测器的贴图）
            MinecraftClient client = MinecraftClient.getInstance();
//            Sprite sprite = client.getBlockRenderManager()
//                    .getModels()
//                    .getModel(Blocks.DETECTOR_RAIL.getDefaultState())
//                    .getSprite(); // 获取侦测器贴图
            Sprite sprite = client.getBlockRenderManager()
                    .getModels()
                    .getModel(Blocks.OBSERVER.getDefaultState())
                    .getParticleSprite();

            RenderSystem.setShaderTexture(0, sprite.getAtlasId());

            // 使用你自定义的 Tessellator
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

            Matrix4f matrix = matrices.peek().getPositionMatrix();

            // 添加顶点（渲染一个平面）
            buffer.vertex(matrix, -0.5f, 0, -0.5f).texture(sprite.getMinU(), sprite.getMinV());
            buffer.vertex(matrix, 0.5f, 0, -0.5f).texture(sprite.getMaxU(), sprite.getMinV());
            buffer.vertex(matrix, 0.5f, 0, 0.5f).texture(sprite.getMaxU(), sprite.getMaxV());
            buffer.vertex(matrix, -0.5f, 0, 0.5f).texture(sprite.getMinU(), sprite.getMaxV());


            BufferRenderer.drawWithGlobalProgram(buffer.end());

            matrices.pop();
        });

    }
}
