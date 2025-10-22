package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import com.yiming.wrutils.data.event.NeighborChangedEvent;
import com.yiming.wrutils.data.event.SimpleEvent;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin {
    @Invoker("isPoweredByOtherRails")
    protected abstract boolean isPoweredByOtherRails1(World world, BlockPos pos, BlockState state, boolean bl, int distance);
    @Inject(method = "updateBlockState", at = @At("HEAD"))
    public void updateBlockStateMixin(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {

        boolean bl = (Boolean)state.get(PoweredRailBlock.POWERED);
        boolean bl2 = world.isReceivingRedstonePower(pos)
                || this.isPoweredByOtherRails1(world, pos, state, true, 0)
                || this.isPoweredByOtherRails1(world, pos, state, false, 0);
        if (bl2 != bl) {
            BlockPos sourcePos = BaseEvent.getFirstFromTop();
            Wrutils.eventRecorder.addEvent(new NeighborChangedEvent(world.getTime(), MicroTimingSequence.NTE, pos,sourcePos, SimpleEvent.EventType.NEIGHBOR_CHANGED));
        }
    }

    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",ordinal = 0,shift = At.Shift.BEFORE))
    public void updateBlockStateMixin1(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("setBlockState Before");

    }
    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",ordinal = 0,shift = At.Shift.AFTER))
    public void updateBlockStateMixin2(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("setBlockState After");
    }
    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V",ordinal = 0,shift = At.Shift.BEFORE))
    public void updateBlockStateMixin3(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("updateNeighborsAlways1 Before");
    }
    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V",ordinal = 0,shift = At.Shift.AFTER))
    public void updateBlockStateMixin4(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("updateNeighborsAlways1 After");
    }

    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V",ordinal = 1,shift = At.Shift.BEFORE))
    public void updateBlockStateMixin5(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("updateNeighborsAlways2 Before");
    }
    @Inject(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V",ordinal = 1,shift = At.Shift.AFTER))
    public void updateBlockStateMixin6(BlockState state, World world, BlockPos pos, Block sourceBlock, CallbackInfo ci) {
//        System.out.println("updateNeighborsAlways2 After");
    }
}
