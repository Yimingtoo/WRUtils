package com.yiming.wrutils.data.event;

import com.yiming.wrutils.data.DataManager;
import org.jetbrains.annotations.Nullable;

public abstract class BaseEvent {
    protected TimeStamp timeStamp;
    protected EventType eventType = EventType.UNKNOWN;
    BlockInfo targetBlockInfo = null;
    BlockInfo sourceBlockInfo = null;

    public BaseEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType) {
        this.targetBlockInfo = targetBlockInfo;
        this.sourceBlockInfo = sourceBlockInfo;
        this.eventType = eventType;

        if (DataManager.lastTimeStamp != null) {
            if (DataManager.lastTimeStamp.gameTime() == gameTime && DataManager.lastTimeStamp.sequence() == microTimingSequence) {
                timeStamp = new TimeStamp(gameTime, microTimingSequence, DataManager.lastTimeStamp.eventId() + 1);
            } else {
                timeStamp = new TimeStamp(gameTime, microTimingSequence, 0);
            }
        } else {
            timeStamp = new TimeStamp(gameTime, microTimingSequence, 0);
        }

        DataManager.lastTimeStamp = timeStamp;

    }


    public static BlockInfo getFirstBlockInfoFromTop() {
        if (!DataManager.BLOCK_INFO_STACK.isEmpty()) {
            return DataManager.BLOCK_INFO_STACK.peek();
        } else {
            return null;
        }
    }


    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public BlockInfo getSourceBlockInfo() {
        return sourceBlockInfo;
    }

    public BlockInfo getTargetBlockInfo() {
        return targetBlockInfo;
    }

    @Override
    public String toString() {

        return String.format("Event: Time=%s,\teventType=%s,\tsource: %s,\ttarget: %s", timeStamp, eventType, sourceBlockInfo, targetBlockInfo);
    }

}
