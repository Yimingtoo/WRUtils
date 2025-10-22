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

@Mixin(targets = "net/minecraft/world/block/ChainRestrictedNeighborUpdater$SixWayEntry")
public class SixWayEntryMixin {
    @Shadow
    private  BlockPos pos;
    @Shadow
    private Block sourceBlock;
    @Nullable
    @Shadow
    private WireOrientation orientation;
    @Nullable
    @Shadow
    private  Direction except;
    @Unique
    private BlockPos sourceBlockPos;

    @Override
    public String toString(){
        return "SixWayEntry{" +
                "pos=" + pos +
                ", sourceBlock=" + sourceBlock +
                ", orientation=" + orientation +
                ", except=" + except +
                '}';
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init1(BlockPos pos, Block sourceBlock, @Nullable WireOrientation orientation, @Nullable Direction except, CallbackInfo ci) {
        sourceBlockPos = BaseEvent.getFirstFromTop();
    }
//    @Inject(method = "update", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,ordinal = 0, target = "Lnet/minecraft/world/block/NeighborUpdater;tryNeighborUpdate(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V"))
//    public void update(World world, CallbackInfoReturnable<Boolean> cir) {
//        BaseEvent.entrySourcePos = this.sourceBlockPos;
//    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update1(World world, CallbackInfoReturnable<Boolean> cir) {
        BaseEvent.entrySourcePos = this.sourceBlockPos;
    }

}
