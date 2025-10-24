package com.yiming.wrutils.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.*;
import com.yiming.wrutils.mixin_interface.WorldTickSchedulerAccessor;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin {


//    @Shadow
//    public static final BooleanProperty POWERED = Properties.POWERED;

//    @Invoker("getUpdateDelayInternal")
//    public abstract int getUpdateDelayInternalMixin(BlockState state);

//    @Invoker("isLocked")
//    public abstract boolean isLockedMixin(WorldView world, BlockPos pos, BlockState state);
//    @Invoker("hasPower")
//    public abstract boolean hasPowerMixin(World world, BlockPos pos, BlockState state) ;


//    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
//    public void scheduledTick1(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        TickPriority priority = TickPriority.VERY_HIGH;
//        int delay = this.getUpdateDelayInternalMixin(state);
//        String description = "计划刻在执行中的添加";
//        EventRecorder.addScheduledTickTag(world, pos, state, delay, priority, description);
//    }
//
//    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
//    public void scheduledTick2(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        EventRecorder.isScheduledTickAdded(world, pos);
//
//    }
//
//    @Inject(method = "updatePowered", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
//    public void updatePowered1(World world, BlockPos pos, BlockState state, CallbackInfo ci, @Local TickPriority tickPriority) {
//        int delay = this.getUpdateDelayInternalMixin(state);
//        String description = "计划刻在执行中的添加";
//        EventRecorder.addScheduledTickTag(world, pos, state, delay, tickPriority, description);
//    }
//
//    @Inject(method = "updatePowered", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
//    public void updatePowered2(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
//        EventRecorder.isScheduledTickAdded(world, pos);
//    }
//

    // -------------------------------------------------------------------------------------------------------------------
    // region Stack source position.
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateMixinHead(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }

    @Inject(method = "neighborUpdate", at = @At("RETURN"))
    public void neighborUpdateMixinReturn(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }


    @Inject(method = "scheduledTick", at = @At("HEAD"))
    public void scheduledTickMixinHead(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
//        System.out.println("AbstractRedstoneGateBlockMixin: sourceBlockPos is " + BaseEvent.entrySourcePos);
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }

    @Inject(method = "scheduledTick", at = @At("RETURN"))
    public void scheduledTickMixinReturn(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }


    @Inject(method = "updateTarget", at = @At("HEAD"))
    public void updateTargetMixinHead(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }

    @Inject(method = "updateTarget", at = @At("RETURN"))
    public void updateTargetMixinReturn(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }
    // endregion
    // -------------------------------------------------------------------------------------------------------------------
}
