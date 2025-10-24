package com.yiming.wrutils.mixin.schedule_tick;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.*;
import com.yiming.wrutils.mixin_interface.ChunkTickScheduleAccessor;
import com.yiming.wrutils.mixin_interface.OrderedTickAccessor;
import com.yiming.wrutils.mixin_interface.WorldTickSchedulerAccessor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Mixin(WorldTickScheduler.class)
@Implements(@Interface(iface = WorldTickSchedulerAccessor.class, prefix = "worldTickScheduler$"))
public class WorldTickSchedulerMixin<T> {
    @Shadow
    private final List<OrderedTick<T>> tickedTicks = new ArrayList<>();
    @Shadow
    private final Long2ObjectMap<ChunkTickScheduler<T>> chunkTickSchedulers = new Long2ObjectOpenHashMap<>();

    @Inject(method = "tick(Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void tick(BiConsumer<BlockPos, T> ticker, CallbackInfo ci) {
        // null
        OrderedTick<T> orderedTick = tickedTicks.getLast();
        if (orderedTick != null) {
            EventRecorder.scheduledTickAddedEventForServerWorld = ((OrderedTickAccessor) (Object) orderedTick).getScheduledTickAddedEvent();
        }
    }

    public long worldTickScheduler$getTicksSize(BlockPos pos) {
        long l = ChunkPos.toLong(pos);
        ChunkTickScheduler<T> chunkTickScheduler = this.chunkTickSchedulers.get(l);
        return ((ChunkTickScheduleAccessor) chunkTickScheduler).getTickQueueSize();
    }

}
