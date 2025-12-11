package com.yiming.wrutils.data.event;

import net.minecraft.world.tick.TickPriority;

public interface ScheduledTickInfo {
    int getDelay();

    TickPriority getPriority();
}
