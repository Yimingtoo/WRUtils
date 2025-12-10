package com.yiming.wrutils.data.event;

import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;

public class ScheduledTickAddEvent extends BaseEvent implements ScheduledTickInfo {
    public enum ScheduledTickAddState {
        NULL,
        TRUE,
        FALSE,
        DEBUG_PROBLEM
    }

    protected int delay;
    protected TickPriority priority;
    protected ScheduledTickAddState isAdded;
    protected String description;

    public ScheduledTickAddEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
    }


    public ScheduledTickAddEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockInfo targetBlockInfo, @Nullable BlockInfo sourceBlockInfo, EventType eventType, ScheduledTickAddState isAdded, int delay, TickPriority priority, String description) {
        super(gameTime, microTimingSequence, targetBlockInfo, sourceBlockInfo, eventType);
        this.isAdded = isAdded;
        this.delay = delay;
        this.priority = priority;
        this.description = description;
    }

    public void setIsAdded(ScheduledTickAddState isAdded) {
        this.isAdded = isAdded;
    }

    public ScheduledTickAddState getIsAdded() {
        return isAdded;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public TickPriority getPriority() {
        return priority;
    }

    @Override
    public String toString() {

        return String.format("Event: Time=%s,\teventType=%s,\tsource: %s,\ttarget: %s,\tdelay=%d,\tpriority=%s,\tisAdded=%s\t%s", timeStamp, eventType, sourceBlockInfo, targetBlockInfo, delay, priority, isAdded, description);
    }
}
