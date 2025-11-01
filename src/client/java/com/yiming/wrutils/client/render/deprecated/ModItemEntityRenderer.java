package com.yiming.wrutils.client.render.deprecated;

import com.yiming.wrutils.data.event.EventRecorder;
import com.yiming.wrutils.entity.ModItemEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@Deprecated
public class ModItemEntityRenderer extends EntityRenderer<ModItemEntity, ModItemEntityRenderState> {
    private final BlockRenderManager blockRenderManager;

    //    ModItemEntity modItemEntity;
    private double lastUpdateTime;

    public ModItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
        System.out.println("============================ ModItemEntityRenderer ==============================");
    }


    @Override
    public ModItemEntityRenderState createRenderState() {
        return new ModItemEntityRenderState();
    }

    @Override
    public void updateRenderState(ModItemEntity entity, ModItemEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        Box box = entity.getBoundingBox();
        Vec3d vec3d = entity.getPos();
//        System.out.println();
//        this.modItemEntity = entity;
//        state.displayName = Text.literal("WRUtils");
//        double d = (double) Util.getMeasuringTimeNano();
//        if (d - this.lastUpdateTime > 1.0E9) {
//            this.lastUpdateTime = d;
////            System.out.printf("%.2f %.2f %.2f %.2f %.2f %.2f%n", modItemEntity.getPos().x, modItemEntity.getPos().y, modItemEntity.getPos().z, entity.getX(), entity.getY(), entity.getZ());
//            entity.setCustomName(Text.literal("WRUtils"));
//
//        }
    }

    public void render(ModItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {

        if (EventRecorder.modItemEntity11 != null) {
            matrices.push();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugFilledBox());
//        VertexConsumer vertexConsumer1 = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
            Box box1 = EventRecorder.modItemEntity11.getBoundingBox();
            Box box = EventRecorder.modItemEntity11.getBoundingBox().offset(-EventRecorder.modItemEntity11.getX(), -EventRecorder.modItemEntity11.getY(), -EventRecorder.modItemEntity11.getZ());

            double minX = box.minX;
            double minY = box.minY;
            double minZ = box.minZ;
            double maxX = box.maxX;
            double maxY = box.maxY;
            double maxZ = box.maxZ;

//        VertexRendering.drawBox(matrices, vertexConsumer1, box, 1, 1, 1, 1.0F);
            VertexRendering.drawFilledBox(matrices, vertexConsumer, 1, 1, 1, -2, -2, -2, 0, 1, 1, 0.30F);
            matrices.pop();

        }
//
//        super.render(state, matrices, vertexConsumerProvider, light);
//
//
//        matrices.push();
//        matrices.translate(0.0, 0, 10);
//        // 示例代码 - 在render方法中添加
//        BlockState repeaterState = Blocks.REPEATER.getDefaultState();
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutout());
//        MinecraftClient.getInstance().getBlockRenderManager()
//                .renderBlock(repeaterState,
//                        BlockPos.ORIGIN,
//                        MinecraftClient.getInstance().world,
//                        matrices,
//                        vertexConsumer,
//                        true,
//                        Random.create());
//        matrices.pop();

    }


}
