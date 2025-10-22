package com.yiming.wrutils.data.event;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;

public class ScheduledTickAddEvent extends BaseEvent {
    protected int delay;
    protected TickPriority priority;

    public ScheduledTickAddEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockPos targetPos, BlockPos sourcePos, EventType eventType) {
        super(gameTime, microTimingSequence, targetPos, sourcePos, eventType);
    }


    @Override
    public void process() {

    }
}
