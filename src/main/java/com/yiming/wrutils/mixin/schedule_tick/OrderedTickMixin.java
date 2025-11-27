package com.yiming.wrutils.mixin.schedule_tick;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import com.yiming.wrutils.mixin_interface.OrderedTickAccessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OrderedTick.class)
@Implements(@Interface(iface = OrderedTickAccessor.class, prefix = "orderedTick$"))
public class OrderedTickMixin<T>{

    @Unique
    public ScheduledTickAddEvent scheduledTickAddedEvent;

    @Inject(method = "<init>(Ljava/lang/Object;Lnet/minecraft/util/math/BlockPos;JLnet/minecraft/world/tick/TickPriority;J)V", at = @At("RETURN"))
    public void init(T object, BlockPos pos, long triggerTick, TickPriority priority, long subTickOrder, CallbackInfo ci) {
        this.scheduledTickAddedEvent = DataManager.scheduledTickAddedEventForOrderedTick;
    }


    public ScheduledTickAddEvent orderedTick$getScheduledTickAddedEvent() {
        return scheduledTickAddedEvent;
    }


}
