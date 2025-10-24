package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPressurePlateBlock.class)
public class AbstractPressurePlateBlockMixin {
    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    public void updateNeighbors(World world, BlockPos pos, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, world.getBlockState(pos)));
    }

    @Inject(method = "updateNeighbors", at = @At("RETURN"))
    public void updateNeighbors1(World world, BlockPos pos, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }
}
