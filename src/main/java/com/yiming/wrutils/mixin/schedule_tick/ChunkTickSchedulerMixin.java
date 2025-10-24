package com.yiming.wrutils.mixin.schedule_tick;

import com.yiming.wrutils.mixin_interface.ChunkTickScheduleAccessor;
import com.yiming.wrutils.mixin_interface.OrderedTickAccessor;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.*;

import java.util.Set;

@Mixin(ChunkTickScheduler.class)
@Implements(@Interface(iface = ChunkTickScheduleAccessor.class, prefix = "chunkTickScheduler$"))
public class ChunkTickSchedulerMixin {
    @Final
    @Shadow
    private Set<OrderedTick<?>> queuedTicks;
    public long chunkTickScheduler$getTickQueueSize() {
        return queuedTicks.size();
    }
}
