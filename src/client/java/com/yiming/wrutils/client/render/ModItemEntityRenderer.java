package com.yiming.wrutils.client.render;

import com.yiming.wrutils.block.entity.ModItemEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public class ModItemEntityRenderer extends EntityRenderer<ModItemEntity, ModItemEntityRenderState> {
    public ModItemEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        System.out.println("============================ ModItemEntityRenderer ==============================");
    }


    @Override
    public ModItemEntityRenderState createRenderState() {
        return new ModItemEntityRenderState();
    }


    public void render(ModItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        System.out.println("============================ render ==============================");
        super.render(state, matrices, vertexConsumers, light);

    }


}
