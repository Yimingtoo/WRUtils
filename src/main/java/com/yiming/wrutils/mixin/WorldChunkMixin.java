package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void setBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, ((WorldChunk) (Object) this).getWorld(), state));
    }

    @Inject(method = "setBlockState", at = @At("RETURN"))
    public void setBlockState1(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
