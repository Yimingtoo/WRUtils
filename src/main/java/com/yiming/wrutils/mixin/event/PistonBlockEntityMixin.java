package com.yiming.wrutils.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import jdk.jfr.Label;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    @Inject(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void finish(CallbackInfo ci, @Local BlockState blockState) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(((PistonBlockEntity) (Object) this).getPos(), blockState));
    }
    // TODO：可能存在注入位置问题
    @Inject(method = "finish", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V"))
    private void finish1(CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();
    }

}
