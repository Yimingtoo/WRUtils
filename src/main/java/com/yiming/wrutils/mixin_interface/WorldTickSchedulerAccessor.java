package com.yiming.wrutils.mixin_interface;

import net.minecraft.util.math.BlockPos;

public interface WorldTickSchedulerAccessor {
    public long getTicksSize(BlockPos pos);
}
