package com.yiming.wrutils.client.render;

import com.yiming.wrutils.entity.ModItemEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ModItemEntityRenderer extends EntityRenderer<ModItemEntity, ModItemEntityRenderState> {

    ModItemEntity modItemEntity;
    private double lastUpdateTime;

    public ModItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        System.out.println("============================ ModItemEntityRenderer ==============================");
    }


    @Override
    public ModItemEntityRenderState createRenderState() {
        return new ModItemEntityRenderState();
    }

    @Override
    public void updateRenderState(ModItemEntity entity, ModItemEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        this.modItemEntity = entity;
        state.displayName = Text.literal("WRUtils") ;
        double d = (double) Util.getMeasuringTimeNano();
        if (d - this.lastUpdateTime > 1.0E9) {
            this.lastUpdateTime = d;
//            System.out.printf("%.2f %.2f %.2f %.2f %.2f %.2f%n", modItemEntity.getPos().x, modItemEntity.getPos().y, modItemEntity.getPos().z, entity.getX(), entity.getY(), entity.getZ());
            entity.setCustomName(Text.literal("WRUtils"));

        }
    }

    public void render(ModItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(state, matrices, vertexConsumerProvider, light);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugFilledBox());
//        VertexConsumer vertexConsumer1 = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
        Box box = this.modItemEntity.getBoundingBox().offset(-this.modItemEntity.getX(), -this.modItemEntity.getY(), -this.modItemEntity.getZ());

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

//        VertexRendering.drawBox(matrices, vertexConsumer1, box, 1, 1, 1, 1.0F);
        VertexRendering.drawFilledBox(matrices, vertexConsumer, minX, minY, minZ, maxX, maxY, maxZ, 1, 0, 0, 0.30F);
    }


}
