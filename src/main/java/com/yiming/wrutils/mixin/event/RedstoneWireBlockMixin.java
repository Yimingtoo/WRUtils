package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    @Inject(method = "update", at = @At("HEAD"))
    public void update(World world, BlockPos pos, BlockState state, @Nullable WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
    }
    @Inject(method = "update", at = @At("RETURN"))
    public void update1(World world, BlockPos pos, BlockState state, @Nullable WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    public void updateNeighbors(World world, BlockPos pos, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    public void updateNeighbors1(World world, BlockPos pos, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
    }

    @Inject(method = "onBlockAdded", at = @At("RETURN"))
    public void onBlockAdded1(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
    }

    @Inject(method = "onStateReplaced", at = @At("RETURN"))
    public void onStateReplaced1(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }

    @Inject(method = "updateForNewState", at = @At("HEAD"))
    public void updateForNewState(World world, BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
    }

    @Inject(method = "updateForNewState", at = @At("RETURN"))
    public void updateForNewState1(World world, BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }

}
