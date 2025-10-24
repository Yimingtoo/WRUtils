package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {

    /**
     * Entry添加方块位置和状态信息
     * @Checked
     * @潜影盒开启的时和完全打开时均会执行 updateNeighborStates
     */
    @Inject(method = "updateNeighborStates", at = @At("HEAD"))
    private static void updateNeighborStates(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "updateNeighborStates", at = @At("RETURN"))
    private static void updateNeighborStates1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }
}
