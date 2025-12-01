package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

    // --------------------------------------------------------------------------------------------------------------------
    // Entry添加方块位置和状态信息
    // TODO:这里的 onSyncedBlockEvent 的 BlockState state 和 BlockPos pos 待检查
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"))
    public void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, state));
    }

    @Inject(method = "onSyncedBlockEvent", at = @At("RETURN"))
    public void onSyncedBlockEvent1(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "move", at = @At("HEAD"))
    public void move(World world, BlockPos pos, Direction dir, boolean extend, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world, world.getBlockState(pos)));
    }

    @Inject(method = "move", at = @At("RETURN"))
    public void move1(World world, BlockPos pos, Direction dir, boolean extend, CallbackInfoReturnable<Boolean> cir) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

}
