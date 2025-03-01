package com.yiming.wrutils.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.BlockPos;

public class BlockOutlineRenderer {

    public void registerBlockOutlineRenderer() {
        WorldRenderEvents.BLOCK_OUTLINE.register((context, blockOutlineContext) -> {

//            BlockPos blockPos = blockOutlineContext.blockPos();
//            System.out.println("BlockOutlineRenderer Pos : " + blockPos.toString());
            return true;
        });
    }
}
