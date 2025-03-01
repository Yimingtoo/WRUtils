package com.yiming.wrutils.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

@Deprecated
public class CustomBlockEntityRenderer implements BlockEntityRenderer<BlockEntity> {
    protected final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;
    private final BlockRenderManager blockRenderManager;

    public CustomBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.dispatcher = ctx.getRenderDispatcher();
        this.textRenderer = ctx.getTextRenderer();
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = blockEntity.getWorld();
        System.out.println("CustomBlockEntityRenderer ================ = = = = = =  = = =W");
        if (world != null) {
            System.out.println("CustomBlockEntityRenderervcbfdgsd");

            BlockPos pos = blockEntity.getPos();
            BlockState state = world.getBlockState(pos);
            // 检查是否是需要渲染文字的方块
            if (world.getBlockState(pos).getBlock() instanceof RepeaterBlock) {
                System.out.println("find RepeaterBlock : " + pos.toString());
//                blockRenderManager.renderBlock(state, pos, world, matrices, vertexConsumers, true, world.random);

                Vec3d renderPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);

                matrices.push();
                matrices.translate(renderPos.x, renderPos.y, renderPos.z);
                matrices.scale(0.025f, 0.025f, 0.025f);

                // 设置文本
                Text text = Text.of("Hello, World!");

                // 渲染文本
//                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-blockEntity.getCachedState().get(CustomBlock.FACING).asRotation()));
//                matrices.translate(-text.getString().length() * 0.0625 / 2, 0, 0);
//                this.dispatcher.drawText(text, 0, 0, 0x000000, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, light);


//                matrices.translate(0.0f, 2f, 0.0f);
                matrices.scale(-0.025f, -0.025f, 0.025f);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
                int j = (int) (g * 255.0f) << 24;
                TextRenderer textRenderer = this.textRenderer;
                float h = (float) -textRenderer.getWidth(text) / 2;
                textRenderer.draw(text, h, 0.0f, 0x20FFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, j, light);


                matrices.pop();
            }
        }
    }

}
