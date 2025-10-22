package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {

//    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("HEAD"))
//    public void setBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("WorldMixin setBlockState HEAD\t"+pos);
//        BaseEvent.BLOCK_POS_STACK.push(pos);
//
//    }
//    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("RETURN"))
//    public void setBlockState1(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("WorldMixin setBlockState RETURN\t"+pos);
//        BaseEvent.BLOCK_POS_STACK.pop();
//
//    }

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
    public void setBlockState2(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        BaseEvent.BLOCK_POS_STACK.push(pos);

    }
    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("RETURN"))
    public void setBlockState3(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        BaseEvent.BLOCK_POS_STACK.pop();

    }
}
