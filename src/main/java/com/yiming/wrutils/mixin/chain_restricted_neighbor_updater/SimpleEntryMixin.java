package com.yiming.wrutils.mixin.chain_restricted_neighbor_updater;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/block/ChainRestrictedNeighborUpdater$SimpleEntry")
public class SimpleEntryMixin {
    @Unique
    private BlockPos sourceBlockPos;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init1(BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, CallbackInfo ci) {
        sourceBlockPos = BaseEvent.getFirstFromTop();
    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update1(World world, CallbackInfoReturnable<Boolean> cir) {
        BaseEvent.entrySourcePos = this.sourceBlockPos;
    }

}
