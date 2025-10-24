package com.yiming.wrutils.data.event;

import org.jetbrains.annotations.Nullable;

public class NeighborChangedEvent extends BaseEvent{


    public NeighborChangedEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
    }
}
