package com.yiming.wrutils.mixin_interface;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import com.yiming.wrutils.data.event.TimeStamp;

public interface OrderedTickAccessor {
    public ScheduledTickAddEvent getScheduledTickAddedEvent();
}