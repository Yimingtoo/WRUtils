package com.yiming.wrutils.mixin.schedule_tick;

import com.yiming.wrutils.data.DataManager;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.QueryableTickScheduler;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.world.tick.TickPriority;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScheduledTickView.class)
public interface ScheduledTickViewMixin {
    @Shadow
    QueryableTickScheduler<Block> getBlockTickScheduler();

    @Invoker("getBlockTickScheduler")
    QueryableTickScheduler<Block> getBlockTickSchedulerMixin();

    @Inject(method = "scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V", at = @At("HEAD"))
    default void scheduleBlockTick1(BlockPos pos, Block block, int delay, TickPriority priority, CallbackInfo ci) {
        if (this.getBlockTickSchedulerMixin() instanceof WorldTickScheduler<Block> worldTickScheduler) {
            DataManager.addScheduledTickAddEvent(
                    worldTickScheduler,
                    pos,
                    ((World) (Object) this).getBlockState(pos),
                    delay,
                    priority
            );

        }

    }

    @Inject(method = "scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V", at = @At("RETURN"))
    default void scheduleBlockTick2(BlockPos pos, Block block, int delay, TickPriority priority, CallbackInfo ci) {
        if (this.getBlockTickSchedulerMixin() instanceof WorldTickScheduler<Block> worldTickScheduler) {
            DataManager.checkIsScheduledTickAddedSuccessfully(worldTickScheduler, pos);
        }
    }

    @Inject(method = "scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V", at = @At("HEAD"))
    default void scheduleBlockTick3(BlockPos pos, Block block, int delay, CallbackInfo ci) {
        if (this.getBlockTickSchedulerMixin() instanceof WorldTickScheduler<Block> worldTickScheduler) {
            DataManager.addScheduledTickAddEvent(
                    worldTickScheduler,
                    pos,
                    ((World) (Object) this).getBlockState(pos),
                    delay,
                    TickPriority.NORMAL
            );
        }

    }

    @Inject(method = "scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V", at = @At("RETURN"))
    default void scheduleBlockTick4(BlockPos pos, Block block, int delay, CallbackInfo ci) {
        if (this.getBlockTickSchedulerMixin() instanceof WorldTickScheduler<Block> worldTickScheduler) {
            DataManager.checkIsScheduledTickAddedSuccessfully(worldTickScheduler, pos);
        }

    }
}
