package com.yiming.wrutils.mixin.chain_restricted_neighbor_updater;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/block/ChainRestrictedNeighborUpdater$StateReplacementEntry")
public class StateReplacementEntryMixin {
    @Unique
    private BlockInfo sourceBlockInfo;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init1(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int updateFlags, int updateLimit, CallbackInfo ci) {
        sourceBlockInfo = BaseEvent.getFirstBlockInfoFromTop();
    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update1(World world, CallbackInfoReturnable<Boolean> cir) {
        EventRecorder.entrySourceBlockInfo = this.sourceBlockInfo;
    }

}
