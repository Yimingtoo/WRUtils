package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    // 执行计划刻
    @Inject(method = "tickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V"))
    private void tickBlock(BlockPos pos, Block block, CallbackInfo ci) {
        ScheduledTickAddEvent event = DataManager.scheduledTickAddedEventForServerWorld;

        ScheduledTickExecEvent event1 = new ScheduledTickExecEvent(
                ((World) (Object) this).getTime(),
                DataManager.currentMicroTimingSequence,
                new BlockInfo(pos, ((World) (Object) this).getBlockState(pos)),
                event == null ? null : event.getTargetBlockInfo(),
                EventType.SCHEDULED_TICK_EXEC,
                event == null ? 0 :event.getDelay(),
                event == null ? null :event.getPriority(),
                "执行计划刻");
        DataManager.eventRecorder.add(event1);

    }


    // --------------------------------------------------------------------------------------------------------------------
    // Micro Timing Sequence

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        // WTU
        DataManager.currentGameTime = ((World) (Object) this).getTime();
        DataManager.currentMicroTimingSequence = MicroTimingSequence.WTU;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE",ordinal = 1, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"))
    private void tick1(CallbackInfo ci) {
        // NTE
        DataManager.currentGameTime = ((World) (Object) this).getTime();
        DataManager.currentMicroTimingSequence = MicroTimingSequence.NTE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 2, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick2(CallbackInfo ci) {
        // RAID
        DataManager.currentMicroTimingSequence = MicroTimingSequence.RAID;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 3, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick3(CallbackInfo ci) {
        // RT
        DataManager.currentMicroTimingSequence = MicroTimingSequence.RT;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 4, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick4(CallbackInfo ci) {
        // BE
        DataManager.currentMicroTimingSequence = MicroTimingSequence.BE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 3, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"))
    private void tick5(CallbackInfo ci) {
        // EU
        DataManager.currentMicroTimingSequence = MicroTimingSequence.EU;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerWorld;tickBlockEntities()V"))
    private void tick6(CallbackInfo ci) {
        // TE
        DataManager.currentMicroTimingSequence = MicroTimingSequence.TE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerEntityManager;tick()V"))
    private void tick7(CallbackInfo ci) {
        // NU
        DataManager.currentMicroTimingSequence = MicroTimingSequence.NU;
    }
}
