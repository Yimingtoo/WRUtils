package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin {
    @Inject(method = "updateCurves", at = @At("HEAD"))
    public void updateCurves(BlockState state, World world, BlockPos pos, boolean notify, CallbackInfoReturnable<BlockState> cir) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));

    }
    @Inject(method = "updateCurves", at = @At("RETURN"))
    public void updateCurves1(BlockState state, World world, BlockPos pos, boolean notify, CallbackInfoReturnable<BlockState> cir) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }
}
