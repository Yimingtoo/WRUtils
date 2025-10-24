package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IceBlock.class)
public class IceBlockMixin {
    @Inject(method = "melt", at = @At("HEAD"))
    private void melt(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "melt", at = @At("RETURN"))
    private void melt1(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }
}
