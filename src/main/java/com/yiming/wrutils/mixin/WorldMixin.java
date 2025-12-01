package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {

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

    @Invoker("getBlockState")
    public abstract BlockState getBlockStateMixin(BlockPos pos);

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
    public void setBlockState2(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, ((World) (Object) this), state));

    }

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("RETURN"))
    public void setBlockState3(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.pop();

    }

    @Inject(method = "updateComparators", at = @At("HEAD"))
    public void updateComparators(BlockPos pos, Block block, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, ((World) (Object) this), this.getBlockStateMixin(pos)));
    }

    @Inject(method = "updateComparators", at = @At("RETURN"))
    public void updateComparators1(BlockPos pos, Block block, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
