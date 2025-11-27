package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.ObserverBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObserverBlock.class)
public class ObserverBlockMixin {
    //    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        EventRecorder.addScheduledTickTag(world, pos, state, 2, "自更新：在计划刻执行中的添加");
//    }
//
//    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
//    public void scheduledTick1(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        EventRecorder.isScheduledTickAdded(world, pos);
//    }
//
//
//    @Inject(method = "scheduleTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/ScheduledTickView;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
//    public void scheduleTick(WorldView world, ScheduledTickView tickView, BlockPos pos, CallbackInfo ci) {
//
//        EventRecorder.addScheduledTickTag(tickView, pos, world.getBlockState(pos), 2, "PP更新");
//    }
//
//    @Inject(method = "scheduleTick", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/tick/ScheduledTickView;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
//    public void scheduleTick1(WorldView world, ScheduledTickView tickView, BlockPos pos, CallbackInfo ci) {
//        EventRecorder.isScheduledTickAdded(tickView, pos);
//    }
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    public void updateNeighbors(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    public void updateNeighbors1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();

    }
}
