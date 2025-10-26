package com.yiming.wrutils.data.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.mixin_interface.WorldTickSchedulerAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.world.tick.TickPriority;
import net.minecraft.world.tick.WorldTickScheduler;

import java.util.ArrayList;
import java.util.Stack;

public class EventRecorder {
    public static long currentGameTime;

    public static Stack<BlockInfo> BLOCK_INFO_STACK = new Stack<>();
    public static MicroTimingSequence currentMicroTimingSequence = MicroTimingSequence.WTU;
    public static BlockInfo entrySourceBlockInfo = null;
    // 执行计划刻
    public static ScheduledTickAddEvent scheduledTickAddedEventForOrderedTick = null;
    public static ScheduledTickAddEvent scheduledTickAddedEventForServerWorld = null;
    private static long tempTickSize;


    ArrayList<BaseEvent> eventList = new ArrayList<>();


    public static void addScheduledTickTag(WorldTickScheduler<Block> worldTickScheduler, BlockPos pos, BlockState state, int delay, TickPriority priority) {
        tempTickSize = ((WorldTickSchedulerAccessor) worldTickScheduler).getTicksSize(pos);
        // 添加计划刻标记
        ScheduledTickAddEvent event = new ScheduledTickAddEvent(
                EventRecorder.currentGameTime,
                EventRecorder.currentMicroTimingSequence,
                new BlockInfo(pos, state),
                EventRecorder.entrySourceBlockInfo,
                EventType.SCHEDULED_TICK_ADD,
                ScheduledTickAddEvent.ScheduledTickAddState.NULL,
                delay,
                priority,
                state.getBlock().toString());
        Wrutils.eventRecorder.addEvent(event);
        EventRecorder.scheduledTickAddedEventForOrderedTick = event;
    }

    /**
     * 必须搭配 {@link #addScheduledTickTag} 使用
     */
    public static void isScheduledTickAdded(WorldTickScheduler<Block> worldTickScheduler, BlockPos pos) {
        long tickSize1 = ((WorldTickSchedulerAccessor) worldTickScheduler).getTicksSize(pos);
        ScheduledTickAddEvent.ScheduledTickAddState addedSuccess;
        if (tickSize1 == tempTickSize) {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.FALSE;
        } else if (tickSize1 - tempTickSize == 1) {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.TRUE;
        } else {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.DEBUG_PROBLEM;
        }
        if (Wrutils.eventRecorder.getLastEvent() instanceof ScheduledTickAddEvent lastEvent) {
            if (lastEvent.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.NULL) {
                lastEvent.setIsAdded(addedSuccess);
            }
        }
    }


    public void addEvent(BaseEvent event) {
        eventList.add(event);
    }

    public void clearEvents() {
        eventList.clear();
    }

    public BaseEvent getEvent(int index) {
        return eventList.get(index);
    }

    public BaseEvent getLastEvent() {
        return eventList.getLast();
    }

    public void printEvents() {
        long lastGameTime = -1;
        for (BaseEvent event : eventList) {
            if (lastGameTime != -1 && event.getTimeStamp().gameTime() != lastGameTime) {
                System.out.println();
            }
            System.out.println(event.toString());

            lastGameTime = event.getTimeStamp().gameTime();
        }
    }


    public int eventsSize() {
        return eventList.size();
    }


//    public void processEvents() {
//        eventList.add(new SimpleEvent(gameTime, microTimingSequence, sourcePos));
//
//        // 通过类型转换访问SimpleEvent特有功能
//        for (
//                BaseEvent event : eventList) {
//            if (event instanceof SimpleEvent) {
//                SimpleEvent simpleEvent = (SimpleEvent) event;
//                // 现在可以访问SimpleEvent的特有数据和方法
//            }
//        }
//    }


}
