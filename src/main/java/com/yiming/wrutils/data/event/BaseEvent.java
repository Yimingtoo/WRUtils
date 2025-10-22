package com.yiming.wrutils.data.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class BaseEvent {
    public static Stack<BlockPos> BLOCK_POS_STACK = new Stack<>();
    public static MicroTimingSequence currentMicroTimingSequence = MicroTimingSequence.WTU;
    public static BlockPos entrySourcePos = BlockPos.ORIGIN;
    public static TimeStamp scheduledEventTimeStamp = null;

    public enum EventType {
        UNKNOWN,
        NEIGHBOR_CHANGED,
        POST_PLACEMENT,
        SCHEDULED_TICK_ADD,
        SCHEDULED_TICK_EXEC,
    }


    protected TimeStamp timeStamp;
    protected EventType eventType = EventType.UNKNOWN;

    protected BlockPos sourcePos;
    protected BlockPos targetPos;
    protected BlockState sourceState = null;
    protected BlockState targetState = null;


    public static TimeStamp lastTimeStamp;

    public BaseEvent(long gameTime, MicroTimingSequence microTimingSequence, BlockPos targetPos, BlockPos sourcePos, EventType eventType) {
        this.targetPos = targetPos;
        this.sourcePos = sourcePos;
        this.eventType = eventType;

        if (lastTimeStamp.gameTime() == gameTime && lastTimeStamp.sequence() == microTimingSequence) {
            timeStamp = new TimeStamp(gameTime, microTimingSequence, lastTimeStamp.eventId()+1);
        }else {
            timeStamp = new TimeStamp(gameTime, microTimingSequence, 0);
        }
        lastTimeStamp = timeStamp;

    }

    public static BlockPos getSecondFromTop() {
        if (BLOCK_POS_STACK.size() >= 2) {
            return BLOCK_POS_STACK.get(BLOCK_POS_STACK.size() - 2);
        } else {
            return null;
        }
    }

    public static BlockPos getFirstFromTop() {
        if (!BLOCK_POS_STACK.isEmpty()) {
            return BLOCK_POS_STACK.peek();
        } else {
            return null;
        }
    }

    public abstract void process();

    public BlockPos getTargetPos() {
        return targetPos;
    }


    @Override
    public String toString() {
        if (sourcePos == null) {
            sourcePos = BlockPos.ORIGIN;
        }
        return String.format("Event: Time=%s,\teventType=%s,\ttargetPos=%s,\tsourcePos=%s", timeStamp, eventType, targetPos.toString(), sourcePos.toString());
    }

}
