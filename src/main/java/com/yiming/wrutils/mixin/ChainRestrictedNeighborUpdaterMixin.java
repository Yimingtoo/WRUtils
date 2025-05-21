package com.yiming.wrutils.mixin;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import net.minecraft.world.block.WireOrientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Mixin(ChainRestrictedNeighborUpdater.class)
public class ChainRestrictedNeighborUpdaterMixin {
    @Shadow
    private int depth;
    @Shadow
    private int maxChainDepth;
    @Shadow
    private World world;
    @Shadow
    private final ArrayDeque<Object> queue = new ArrayDeque();


//    @Inject(method = "enqueue", at = @At("HEAD"))
//    private void enqueue1(BlockPos pos, Object entry, CallbackInfo ci) {
//        System.out.println("enqueue--------------------------------------------- : " + this.depth);
//    }

    @Inject(method = "updateNeighbors", at = @At("HEAD"))
    private void updateNeighbors1(BlockPos pos, Block sourceBlock, Direction except, WireOrientation orientation, CallbackInfo ci) {
        System.out.println("Tick : " + this.world.getTime() + "\t" + "updateNeighbors : " + this.depth + sourceBlock.toString() + "       maxChainDepth: " + this.maxChainDepth);
    }

    @Inject(method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V", at = @At("HEAD"))
    private void updateNeighbor1(BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, CallbackInfo ci) {
        System.out.println("Tick : " + this.world.getTime() + "\t" + "updateNeighbor1 : " + this.depth + sourceBlock.toString() + "       maxChainDepth: " + this.maxChainDepth);
    }

    @Inject(method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V", at = @At("HEAD"))
    private void updateNeighbor2(BlockState state, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        System.out.println("Tick : " + this.world.getTime() + "\t" + "updateNeighbor2 : " + this.depth + sourceBlock.toString() + "       maxChainDepth: " + this.maxChainDepth);
    }

    @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
    private void replaceWithStateForNeighborUpdate1(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth, CallbackInfo ci) {
        System.out.println("Tick : " + this.world.getTime() + "\t" + "replaceWithStateForNeighborUpdate1 : " + this.depth + pos.toString() + "       maxChainDepth: " + maxUpdateDepth);
    }

    @Inject(method = "runQueuedUpdates", at = @At("HEAD"))
    private void runQueuedUpdates1(CallbackInfo ci) {
        System.out.println("Tick : " + this.world.getTime() + "\t" + "runQueuedUpdates1 : " + this.depth + " " + this.queue.size() + "       maxChainDepth: " + this.maxChainDepth);

    }

}
