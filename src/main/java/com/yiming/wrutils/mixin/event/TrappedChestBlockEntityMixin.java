package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrappedChestBlockEntity.class)
public class TrappedChestBlockEntityMixin {
    @Inject(method = "onViewerCountUpdate", at = @At("HEAD"))
    private void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "onViewerCountUpdate", at = @At("RETURN"))
    private void onViewerCountUpdate1(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
