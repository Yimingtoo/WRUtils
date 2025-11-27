package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BlockInfo;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    /**
     * Entry添加方块位置和状态信息;
     * NC更新：world.updateNeighbors(pos, Blocks.AIR);
     * PP更新：state.updateNeighbors(world, pos, Block.NOTIFY_ALL);
     *
     * @Checked
     * @world.setBlockState(blockPos, state.with(PART, BedPart.HEAD), Block.NOTIFY_ALL); //设置床头方块
     * @world.updateNeighbors(pos, Blocks.AIR); // 床尾方块的NC更新
     * @state.updateNeighbors(world, pos, Block.NOTIFY_ALL); // 床尾方块的PP更新
     */
    @Inject(method = "onPlaced", at = @At("HEAD"))
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, state));
    }

    @Inject(method = "onPlaced", at = @At("RETURN"))
    public void onPlaced1(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }
}
