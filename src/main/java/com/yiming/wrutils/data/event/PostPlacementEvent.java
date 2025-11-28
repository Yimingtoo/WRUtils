package com.yiming.wrutils.data.event;

import org.jetbrains.annotations.Nullable;

public class PostPlacementEvent extends BaseEvent{
    public PostPlacementEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
    }
}
