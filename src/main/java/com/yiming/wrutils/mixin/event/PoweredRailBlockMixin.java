package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin {
    @Inject(method = "updateBlockState", at = @At("HEAD"))
    public void updateBlockStateMixin(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "updateBlockState", at = @At("RETURN"))
    public void updateBlockStateMixin1(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }
}
