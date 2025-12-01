package com.yiming.wrutils.client.render;

import com.yiming.wrutils.client.utils.WrutilsClientUtils;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.model.BakedModel;

public class CustomRender {
    public static double x_cr = 1;
    public static BakedModel model;

    public static boolean isRender = true;

    public static void renderCustomModelOut() {
        WorldRenderEvents.LAST.register(context -> {
            isRender = true;
            if (!DataManager.isSelectionEnabled) {
                return;
            }
            SelectBoxes selectBoxes = DataManager.areaGroupManagement.getCurrentBoxes();
            if (selectBoxes == null) {
                return;
            }
            if (selectBoxes.getList().isEmpty()) {
                return;
            }

            for (SelectBox box : selectBoxes.getList(WrutilsClientUtils.getPlayerDimension())) {
                boolean isCurrent = box == selectBoxes.getCurrentSelectBox();
                DrawColor style = isCurrent ? new DrawColor(DrawColor.PINK, 0.25f) : new DrawColor(DrawColor.LIGHT_BLUE, 0.25f);
                ZoneRenderer.drawSelectedBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        box.pos1(), new DrawColor(DrawColor.RED, 1f),
                        box.pos2(), new DrawColor(DrawColor.BLUE, 1f),
                        style
                );
                ZoneRenderer.drawBoxFaces(context.matrixStack(), context.camera(),
                        box.pos1(),
                        box.pos2(),
                        style
                );
                if (isCurrent && box.getMoveCtrlPos() != null) {
                    ZoneRenderer.drawSelectedBox(context.matrixStack(), context.camera(), context.consumers(),
                            box.getMoveCtrlPos(),
                            new DrawColor(0x629755, 1f),
                            box.getMoveCtrlPos(),
                            new DrawColor(0x629755, 1f),
                            new DrawColor(0x629755, 1f)
                    );
                    ZoneRenderer.drawBoxFaces(context.matrixStack(), context.camera(),
                            box.getMoveCtrlPos(),
                            new DrawColor(DrawColor.RED, .3f)
                    );
                }
            }
        });
    }

}
