package com.yiming.wrutils.mixin.chain_restricted_neighbor_updater;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import net.minecraft.world.block.WireOrientation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    @Final
    @Shadow
    private int maxChainDepth;
    @Final
    @Shadow
    private World world;
    @Shadow
    private final ArrayDeque<Object> queue = new ArrayDeque<>();
    @Shadow
    private final List<Object> pending = new ArrayList<>();

    @Inject(method = "runQueuedUpdates", at = @At(value = "INVOKE", target = "Ljava/util/ArrayDeque;peek()Ljava/lang/Object;", shift = At.Shift.AFTER, ordinal = 0))
    private void runQueuedUpdates2(CallbackInfo ci) {

        System.out.println("runQueuedUpdates1 : " +"    pending num : "+this.pending.size()+"   queue num : "+this.queue.size()+"   "+this.queue.peek().toString());

    }

    @Inject(method = "runQueuedUpdates", at = @At(value = "INVOKE", target = "Ljava/util/ArrayDeque;isEmpty()Z", shift = At.Shift.AFTER, ordinal = 0))
    private void runQueuedUpdates3(CallbackInfo ci) {

        System.out.println("runQueuedUpdates2--------------- : " +"    pending num : "+this.pending.size()+"   queue num : "+this.queue.size()+"   ");
    }
}
