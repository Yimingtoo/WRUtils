package com.yiming.wrutils.mixin_interface;

import com.yiming.wrutils.data.event.TimeStamp;

public interface OrderedTickAccessor {
    TimeStamp getScheduledTimeStamp();
}