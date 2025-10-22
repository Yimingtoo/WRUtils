package com.yiming.wrutils.mixin.schedule_tick;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Mixin(WorldTickScheduler.class)
public class WorldTickSchedulerMixin<T> {
    @Shadow
    private final List<OrderedTick<T>> tickedTicks = new ArrayList<>();

    @Inject(method = "tick(Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void tick(BiConsumer<BlockPos, T> ticker, CallbackInfo ci) {
        // null
        OrderedTick<T> orderedTick = tickedTicks.get(tickedTicks.size() - 1);
        if (orderedTick != null) {
//            long id = ((OrderedTickAccessor)(Object) orderedTick).getId();
//            System.out.println("tick: " + id);
        }


    }
}
