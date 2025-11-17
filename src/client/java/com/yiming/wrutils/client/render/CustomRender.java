package com.yiming.wrutils.client.render;

import com.yiming.wrutils.data.select_box.SelectBoxes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.Vec3i;

public class CustomRender {
    public static double x_cr = 1;
    public static BakedModel model;

    public static boolean isRender = true;

    public static void renderCustomModelOut() {
        WorldRenderEvents.LAST.register(context -> {
            isRender = true;
            if (isRender) {
                Vec3i pos1 = SelectBoxes.getCurrent().pos1();
                Vec3i pos2 = SelectBoxes.getCurrent().pos2();
                ZoneRenderer3.drawSelectedBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        pos1, new DrawColor(DrawColor.RED, 1f),
                        pos2, new DrawColor(DrawColor.BLUE, 1f),
                        new DrawColor(DrawColor.WHITE, 1f));
                ZoneRenderer3.drawBoxFaces(context.matrixStack(), context.camera(),
                        pos1,
                        pos2,
                        new DrawColor(DrawColor.WHITE, 0.2f)
                );

                ZoneRenderer3.drawSelectedBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        new Vec3i(0, 0, 0), new DrawColor(DrawColor.RED, 1f),
                        new Vec3i(2, 2, 2), new DrawColor(DrawColor.BLUE, 1f),
                        new DrawColor(DrawColor.WHITE, 1f));
                ZoneRenderer3.drawBoxFaces(context.matrixStack(), context.camera(),
                        new Vec3i(0, 0, 0),
                        new Vec3i(2, 2, 2),
                        new DrawColor(DrawColor.WHITE, 0.2f)
                );


            }
        });

    }


}
