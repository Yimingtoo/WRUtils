package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {
    @Inject(method = "updateNeighborAlways", at = @At("HEAD"))
    private static void updateNeighborAlways(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "updateNeighborAlways", at = @At("RETURN"))
    private static void updateNeighborAlways1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }
}
