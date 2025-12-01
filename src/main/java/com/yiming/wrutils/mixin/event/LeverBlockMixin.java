package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeverBlock.class)
public class LeverBlockMixin {
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    public void updateNeighbors(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, state));
    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    public void updateNeighbors1(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
