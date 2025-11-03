package com.yiming.wrutils.client.render;

import com.yiming.wrutils.data.select_box.SelectBox;
import com.yiming.wrutils.data.select_box.SelectBoxes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class CustomRender {
    public static double x_cr = 1;
    public static BakedModel model;

    public static boolean isRender = true;


    public static void renderCustomModelOut() {
        WorldRenderEvents.LAST.register(context -> {
            if (isRender) {
                Vec3i pos1 = SelectBoxes.getCurrent().pos1();
                Vec3i pos2 = SelectBoxes.getCurrent().pos2();

//                ZoneRenderer.drawSelectedBox(
//                        context.matrixStack(), context.camera(), context.consumers(),
//                        pos1, new DrawStyle(DrawStyle.RED, 0.5f),
//                        pos2, new DrawStyle(DrawStyle.BLUE, 0.5f),
//                        new DrawStyle(DrawStyle.WHITE, 1f)
//                );
//
//
//                ZoneRenderer.drawFilledBox(
//                        context.matrixStack(), context.camera(), context.consumers(),
//                        pos1,
//                        pos2,
//                        new DrawStyle(DrawStyle.WHITE, 0.2f)
//                );
                ZoneRenderer2.drawSelectedBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        pos1, new DrawStyle(DrawStyle.RED, 1f),
                        pos2, new DrawStyle(DrawStyle.BLUE, 1f),
                        new DrawStyle(DrawStyle.WHITE, 1f));
                ZoneRenderer2.drawFilledBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        pos1,
                        pos2,
                        new DrawStyle(DrawStyle.WHITE, 0.2f)
                );




            }
        });

    }


}
