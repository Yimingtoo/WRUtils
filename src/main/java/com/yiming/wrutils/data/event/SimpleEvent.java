package com.yiming.wrutils.data.event;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class SimpleEvent extends BaseEvent{


    public SimpleEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockPos targetPos, BlockPos sourcePos, EventType eventType) {
        super(gameTime, microTimingSequence, targetPos, sourcePos, eventType);
    }

    @Override
    public void process() {

    }
}
