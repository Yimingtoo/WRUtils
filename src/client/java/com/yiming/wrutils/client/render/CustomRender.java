package com.yiming.wrutils.client.render;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.client.utils.WrutilsClientUtils;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.Dimension;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class CustomRender {
    public static void renderCustomModelOut() {
        WorldRenderEvents.LAST.register(context -> {
            if (context.matrixStack() == null || context.consumers() == null) {
                return;
            }
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
                WrutilsColor style = isCurrent ? new WrutilsColor(WrutilsColor.PINK, 0.25f) : new WrutilsColor(WrutilsColor.LIGHT_BLUE, 0.25f);
                ZoneRenderer.drawSelectedBox(
                        context.matrixStack(), context.camera(), context.consumers(),
                        box.pos1(), new WrutilsColor(WrutilsColor.RED, 1f),
                        box.pos2(), new WrutilsColor(WrutilsColor.BLUE, 1f),
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
                            new WrutilsColor(0x629755, 1f),
                            box.getMoveCtrlPos(),
                            new WrutilsColor(0x629755, 1f),
                            new WrutilsColor(0x629755, 1f)
                    );
                    ZoneRenderer.drawBoxFaces(context.matrixStack(), context.camera(),
                            box.getMoveCtrlPos(),
                            new WrutilsColor(WrutilsColor.RED, .3f)
                    );
                }
            }
        });
    }

    public static void renderEvent() {
        WorldRenderEvents.LAST.register(context -> {
            if (context.matrixStack() == null || context.consumers() == null) {
                return;
            }
            if (!DataManagerClient.isFilterEventRender) {
                return;
            }
            // 获取玩家的维度
            Dimension dimension = WrutilsClientUtils.getPlayerDimension();

            if (DataManagerClient.filterEventPointer < DataManagerClient.filterEventList.size()) {
                BaseEvent event = DataManagerClient.filterEventList.get(DataManagerClient.filterEventPointer);
                BlockInfo targetBlockInfo = event.getTargetBlockInfo();
                BlockInfo sourceBlockInfo = event.getSourceBlockInfo();
                if (targetBlockInfo.dimension() == dimension) {
                    ZoneRenderer.drawBoxFaces(context.matrixStack(), context.camera(),
                            targetBlockInfo.pos(),
                            new WrutilsColor(WrutilsColor.RED, .3f)
                    );
                    if (sourceBlockInfo != null) {
                        ZoneRenderer.drawBoxFaces(context.matrixStack(), context.camera(),
                                sourceBlockInfo.pos(),
                                new WrutilsColor(WrutilsColor.GREEN, .3f)
                        );
                    }
                }
            }


        });


    }


}
