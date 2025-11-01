package com.yiming.wrutils.client.render.deprecated;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

@Deprecated
public class BlockOutlineRenderer {

    public void registerBlockOutlineRenderer() {
        WorldRenderEvents.BLOCK_OUTLINE.register((context, blockOutlineContext) -> {

//            BlockPos blockPos = blockOutlineContext.blockPos();
//            System.out.println("BlockOutlineRenderer Pos : " + blockPos.toString());
            return true;
        });
    }
}
