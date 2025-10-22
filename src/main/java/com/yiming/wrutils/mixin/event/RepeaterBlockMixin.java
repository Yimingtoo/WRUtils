package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepeaterBlock.class)
public class RepeaterBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"))
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit,  CallbackInfoReturnable<ActionResult> cir) {
        BaseEvent.BLOCK_POS_STACK.push(pos);

    }
    @Inject(method = "onUse", at = @At("RETURN"))
    public void onUse1(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit,  CallbackInfoReturnable<ActionResult> cir) {
        BaseEvent.BLOCK_POS_STACK.pop();

    }
}
