package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkSensorBlock.class)
public class SculkSensorBlockMixin {
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    private static void updateNeighbors(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, state));
    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    private static void updateNeighbors1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
