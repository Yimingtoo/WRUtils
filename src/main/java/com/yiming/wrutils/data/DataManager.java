package com.yiming.wrutils.data;

import com.yiming.wrutils.data.event.*;
import com.yiming.wrutils.data.selected_area.AreaGroupManagement;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import com.yiming.wrutils.mixin_interface.WorldTickSchedulerAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;
import net.minecraft.world.tick.WorldTickScheduler;

import java.util.ArrayList;
import java.util.Stack;

public class DataManager {

    public static long currentGameTime;
    public static Stack<BlockInfo> BLOCK_INFO_STACK = new Stack<>();
    public static MicroTimingSequence currentMicroTimingSequence = MicroTimingSequence.WTU;
    public static BlockInfo entrySourceBlockInfo = null;
    // 执行计划刻
    public static ScheduledTickAddEvent scheduledTickAddedEventForOrderedTick = null;
    public static ScheduledTickAddEvent scheduledTickAddedEventForServerWorld = null;
    public static long tempTickSize;


    public static AreaGroupManagement areaGroupManagement = new AreaGroupManagement();
    public static ArrayList<BaseEvent> eventRecorder = new ArrayList<>();

    public static boolean isSelectionEnabled = false;
    public static boolean isRecording = false;


    public static TimeStamp lastTimeStamp;

    private static boolean shouldRecord(BlockPos pos) {
        if (!isRecording) {
            return false;
        }
        if (!isSelectionEnabled) {
            return false;
        }
        for (SelectBox selectBox : getCurrentBoxes().getList()) {
            if (selectBox.isContainVec3iPos(pos)) {
                return true;
            }
        }
        return false;
    }

    public static void addNeighborChangedEvent(BlockPos pos, BlockState state) {
        if (!shouldRecord(pos)) {
            return;
        }
        NeighborChangedEvent event = new NeighborChangedEvent(
                currentGameTime,
                currentMicroTimingSequence,
                new BlockInfo(pos, state),
                entrySourceBlockInfo,
                EventType.NEIGHBOR_CHANGED);
        eventRecorder.add(event);
    }

    public static void addPostPlacementEvent(BlockPos pos, BlockState state) {
        if (!shouldRecord(pos)) {
            return;
        }
        PostPlacementEvent event = new PostPlacementEvent(
                currentGameTime,
                currentMicroTimingSequence,
                new BlockInfo(pos, state),
                entrySourceBlockInfo,
                EventType.POST_PLACEMENT);
        eventRecorder.add(event);
    }

    public static void addScheduleTickExecEvent(BlockPos pos, BlockState state) {
        if (!shouldRecord(pos)) {
            return;
        }
        ScheduledTickAddEvent event = scheduledTickAddedEventForServerWorld;

        ScheduledTickExecEvent event1 = new ScheduledTickExecEvent(
                currentGameTime,
                currentMicroTimingSequence,
                new BlockInfo(pos, state),
                event == null ? null : event.getTargetBlockInfo(),
                EventType.SCHEDULED_TICK_EXEC,
                event == null ? 0 : event.getDelay(),
                event == null ? null : event.getPriority(),
                "执行计划刻");
        eventRecorder.add(event1);
    }

    public static void addScheduledTickAddEvent(WorldTickScheduler<Block> worldTickScheduler, BlockPos pos, BlockState state, int delay, TickPriority priority) {
        if (!shouldRecord(pos)) {
            return;
        }
        tempTickSize = ((WorldTickSchedulerAccessor) worldTickScheduler).getTicksSize(pos);
        // 添加计划刻标记
        ScheduledTickAddEvent event = new ScheduledTickAddEvent(
                currentGameTime,
                currentMicroTimingSequence,
                new BlockInfo(pos, state),
                entrySourceBlockInfo,
                EventType.SCHEDULED_TICK_ADD,
                ScheduledTickAddEvent.ScheduledTickAddState.NULL,
                delay,
                priority,
                state.getBlock().toString());
        eventRecorder.add(event);
        scheduledTickAddedEventForOrderedTick = event;
    }

    /**
     * 必须搭配 {@link #addScheduledTickAddEvent} 使用
     */
    public static void checkIsScheduledTickAddedSuccessfully(WorldTickScheduler<Block> worldTickScheduler, BlockPos pos) {
        if (!shouldRecord(pos)) {
            return;
        }
        long tickSize1 = ((WorldTickSchedulerAccessor) worldTickScheduler).getTicksSize(pos);
        ScheduledTickAddEvent.ScheduledTickAddState addedSuccess;
        if (tickSize1 == tempTickSize) {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.FALSE;
        } else if (tickSize1 - tempTickSize == 1) {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.TRUE;
        } else {
            addedSuccess = ScheduledTickAddEvent.ScheduledTickAddState.DEBUG_PROBLEM;
        }
        if (eventRecorder.getLast() instanceof ScheduledTickAddEvent lastEvent) {
            if (lastEvent.getIsAdded() == ScheduledTickAddEvent.ScheduledTickAddState.NULL) {
                lastEvent.setIsAdded(addedSuccess);
            }
        }
    }

    public static SelectBoxes getCurrentBoxes() {
        return areaGroupManagement.getCurrentBoxes();
    }

    public static SelectBox getCurrentSelectBox() {
        if (areaGroupManagement.getCurrentBoxes() != null) {
            return areaGroupManagement.getCurrentBoxes().getCurrentSelectBox();
        }
        return null;
    }

    public static void printEvents() {
        long lastGameTime = -1;
        for (BaseEvent event : eventRecorder) {
            if (lastGameTime != -1 && event.getTimeStamp().gameTime() != lastGameTime) {
                System.out.println();
            }
            System.out.println(event.toString());

            lastGameTime = event.getTimeStamp().gameTime();
        }
    }


}
