package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.Dimension;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.util.collection.Weight;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "postProcessState", at = @At("HEAD"))
    private static void postProcessState(BlockState state, WorldAccess world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (world instanceof World world1) {
            DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world1, state));
        } else {
            DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, Dimension.NONE, state));
        }
    }

    @Inject(method = "postProcessState", at = @At("RETURN"))
    private static void postProcessState1(BlockState state, WorldAccess world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        // TODO: need check
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
