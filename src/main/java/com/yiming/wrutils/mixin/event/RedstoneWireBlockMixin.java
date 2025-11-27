package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    @Inject(method = "prepare", at = @At("HEAD"))
    public void prepare(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }
    @Inject(method = "prepare", at = @At("RETURN"))
    public void prepare1(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update(World world, BlockPos pos, BlockState state, @Nullable WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }
    @Inject(method = "update", at = @At("RETURN"))
    public void update1(World world, BlockPos pos, BlockState state, @Nullable WireOrientation orientation, boolean blockAdded, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    public void updateNeighbors(World world, BlockPos pos, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, Blocks.REDSTONE_WIRE.getDefaultState()));
    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    public void updateNeighbors1(World world, BlockPos pos, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();

    }

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "onBlockAdded", at = @At("RETURN"))
    public void onBlockAdded1(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "onStateReplaced", at = @At("RETURN"))
    public void onStateReplaced1(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "updateForNewState", at = @At("HEAD"))
    public void updateForNewState(World world, BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, newState));

    }

    @Inject(method = "updateForNewState", at = @At("RETURN"))
    public void updateForNewState1(World world, BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

}
