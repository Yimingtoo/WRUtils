package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        // WTU
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.WTU;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/world/ServerWorld;calculateAmbientDarkness()V"))
    private void tick1(CallbackInfo ci) {
        // NTE
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.NTE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 2, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick2(CallbackInfo ci) {
        // RAID
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.RAID;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 3, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick3(CallbackInfo ci) {
        // RT
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.RT;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 4, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void tick4(CallbackInfo ci) {
        // BE
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.BE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 3, shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"))
    private void tick5(CallbackInfo ci) {
        // EU
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.EU;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerWorld;tickBlockEntities()V"))
    private void tick6(CallbackInfo ci) {
        // TE
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.TE;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerEntityManager;tick()V"))
    private void tick7(CallbackInfo ci) {
        // NU
        BaseEvent.currentMicroTimingSequence= MicroTimingSequence.NU;
    }
}
