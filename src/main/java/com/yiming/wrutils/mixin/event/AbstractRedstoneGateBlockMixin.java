package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.UpdateInfo;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin {

    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateMixinHead(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);

    }
    @Inject(method = "neighborUpdate", at = @At("RETURN"))
    public void neighborUpdateMixinReturn(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();

    }


    @Inject(method = "scheduledTick", at = @At("HEAD"))
    public void scheduledTickMixinHead(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        System.out.println("AbstractRedstoneGateBlockMixin: sourceBlockPos is "+BaseEvent.entrySourcePos);
        BaseEvent.BLOCK_POS_STACK.push(pos);

    }
    @Inject(method = "scheduledTick", at = @At("RETURN"))
    public void scheduledTickMixinReturn(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();

    }


    @Inject(method = "updateTarget", at = @At("HEAD"))
    public void updateTargetMixinHead(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);

    }
    @Inject(method = "updateTarget", at = @At("RETURN"))
    public void updateTargetMixinReturn(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();

    }
}
