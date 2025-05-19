package com.yiming.wrutils.data;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方块的仅一个更新的信息<br>
 * 包括更新的类型，更新产生的计划刻
 */
public class UpdateInfo {
    public enum UpdateType {NC, PP, S;}

    private static final AtomicInteger ORDER = new AtomicInteger();
    private static long currentGameTick;
    private int order;
    private long gameTick;

    private BlockPos sourceBlockPos;
    private TickScheduleInfo tickScheduleInfo;

    private UpdateType updateType;

    public UpdateInfo(long gameTick) {
        if (gameTick != UpdateInfo.currentGameTick) {
            UpdateInfo.currentGameTick = gameTick;
            ORDER.set(0);
        }
        this.order = ORDER.getAndIncrement();
        this.gameTick = gameTick;
        this.tickScheduleInfo = new TickScheduleInfo();
    }

    //Getter
    public long getGameTick() {
        return gameTick;
    }

    public int getOrder() {
        return order;
    }


    public TickScheduleInfo getTickScheduleInfo() {
        return tickScheduleInfo;
    }

    //Setter
    public void setTickScheduleInfo(Boolean isAddScheduleSuccess, TickPriority tickPriority, int delay, String tickDescription){
        this.tickScheduleInfo.setAddScheduleSuccess(isAddScheduleSuccess);
        this.tickScheduleInfo.setTickPriority(tickPriority);
        this.tickScheduleInfo.setDelay(delay);
        this.tickScheduleInfo.setTickDescription(tickDescription);
    }

    class TickScheduleInfo {
        private boolean isAddScheduleSuccess;
        private TickPriority tickPriority;
        private int delay;
        private String tickDescription;

        //Getter
        public boolean isAddScheduleSuccess() {
            return isAddScheduleSuccess;
        }

        public TickPriority getTickPriority() {
            return tickPriority;
        }

        public int getDelay() {
            return delay;
        }

        public String getTickDescription() {
            return tickDescription;
        }

        // Setter
        public void setAddScheduleSuccess(boolean addScheduleSuccess) {
            isAddScheduleSuccess = addScheduleSuccess;
        }

        public void setTickPriority(TickPriority tickPriority) {
            this.tickPriority = tickPriority;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public void setTickDescription(String tickDescription) {
            this.tickDescription = tickDescription;
        }

    }


}
