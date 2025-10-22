package com.yiming.wrutils.mixin.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin {

    @Inject(method = "tryNeighborUpdate", at = @At("HEAD"))
    private static void tryNeighborUpdate1(World world, BlockState state, BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, boolean notify, CallbackInfo ci) {
//        System.out.println("tryNeighborUpdate1");
    }
}
