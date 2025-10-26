package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin {

    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        System.out.println("AbstractRailBlockMixin :   NC update");
    }
    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"))
    public void getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random, CallbackInfoReturnable<BlockState> cir) {
        System.out.println("AbstractRailBlockMixin :   PP update");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Inject(method = "updateCurves", at = @At("HEAD"))
    public void updateCurves(BlockState state, World world, BlockPos pos, boolean notify, CallbackInfoReturnable<BlockState> cir) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }
    @Inject(method = "updateCurves", at = @At("RETURN"))
    public void updateCurves1(BlockState state, World world, BlockPos pos, boolean notify, CallbackInfoReturnable<BlockState> cir) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }
}
