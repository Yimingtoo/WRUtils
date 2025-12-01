package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DetectorRailBlock.class)
public class DetectorRailBlockMixin {
    @Inject(method = "updatePoweredStatus", at = @At("HEAD"))
    public void updatePoweredStatus(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, state));
    }

    @Inject(method = "updatePoweredStatus", at = @At("RETURN"))
    public void updatePoweredStatus1(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "updateNearbyRails", at = @At("HEAD"))
    public void updateNearbyRails(World world, BlockPos pos, BlockState state, boolean unpowering, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, state));
    }

    @Inject(method = "updateNearbyRails", at = @At("RETURN"))
    public void updateNearbyRails1(World world, BlockPos pos, BlockState state, boolean unpowering, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
