package com.yiming.wrutils.data.event;

import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;

public class ScheduledTickExecEvent extends BaseEvent {

    protected int delay;
    protected TickPriority priority;
    protected String description;

    public ScheduledTickExecEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
    }


    public ScheduledTickExecEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo,@Nullable  BlockInfo sourceBlockInfo, EventType eventType, int delay, TickPriority priority, String description) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
        this.delay = delay;
        this.priority = priority;
        this.description = description;
    }

    public int getDelay() {
        return delay;
    }

    public TickPriority getPriority() {
        return priority;
    }


    @Override
    public String toString() {

        return String.format("Event: Time=%s,\teventType=%s,\tsource: %s,\ttarget: %s,\tdelay=%d,\tpriority=%s,\t%s", timeStamp, eventType, sourceBlockInfo, targetBlockInfo, delay, priority, description);
    }
}
