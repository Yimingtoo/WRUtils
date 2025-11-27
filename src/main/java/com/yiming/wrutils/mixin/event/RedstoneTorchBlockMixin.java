package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {
    /**
     * 这个可以不用添加，因为 update 在 onBlockAdded 和 onStateReplaced 中执行
     */
    @Inject(method = "update", at = @At("HEAD"))
    private void update(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void update1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
