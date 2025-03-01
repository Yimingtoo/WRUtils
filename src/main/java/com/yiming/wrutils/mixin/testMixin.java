package com.yiming.wrutils.mixin;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.block.entity.ModItemEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepeaterBlock.class)
public class testMixin {


    @Inject(at = @At("HEAD"), method = "onUse")
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ModItemEntity modItemEntity = new ModItemEntity(Wrutils.MOD_ITEM_ENTITY_ENTITY_TYPE, world);
        System.out.println("testMixin clicked");
    }
}
