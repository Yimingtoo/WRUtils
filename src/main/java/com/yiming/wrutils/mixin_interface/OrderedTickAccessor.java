package com.yiming.wrutils.mixin_interface;

import com.yiming.wrutils.data.event.ScheduledTickAddEvent;

public interface OrderedTickAccessor {
    ScheduledTickAddEvent getScheduledTickAddedEvent();
}