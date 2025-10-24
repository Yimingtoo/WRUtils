package com.yiming.wrutils.mixin.chain_restricted_neighbor_updater;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/block/ChainRestrictedNeighborUpdater$StatefulEntry")
public class StatefulEntryMixin {
    @Unique
    private BlockInfo sourceBlockInfo;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init1(BlockState state, BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, boolean movedByPiston, CallbackInfo ci) {
        sourceBlockInfo = BaseEvent.getFirstBlockInfoFromTop();
    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update1(World world, CallbackInfoReturnable<Boolean> cir) {
        EventRecorder.entrySourceBlockInfo = this.sourceBlockInfo;

    }

}
