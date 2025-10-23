package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.SimpleEvent;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @see net.minecraft.block.AbstractBlock
 */
@Mixin(targets = "net/minecraft/block/AbstractBlock$AbstractBlockState")
public class AbstractBlockStateMixin {
    /**
     * @see net.minecraft.block.AbstractBlock.AbstractBlockState#neighborUpdate(World, BlockPos, Block, WireOrientation, boolean)
     */
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdate(World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.push(pos);
        if (isNeededBlock(((AbstractBlock.AbstractBlockState) (Object) this).getBlock())) {
            Wrutils.eventRecorder.addEvent(new SimpleEvent(world.getTime(), BaseEvent.currentMicroTimingSequence, pos, BaseEvent.entrySourcePos, SimpleEvent.EventType.NEIGHBOR_CHANGED));
        }
    }

    @Inject(method = "neighborUpdate", at = @At("RETURN"))
    public void neighborUpdate1(World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
    }

    @Unique
    private boolean isNeededBlock(Block block) {
        return block instanceof AbstractRailBlock ||
                block instanceof AbstractRedstoneGateBlock ||
                block instanceof AbstractSkullBlock ||
                block instanceof BellBlock ||
                block instanceof BigDripleafBlock ||
                block instanceof BulbBlock ||
                block instanceof CommandBlock ||
                block instanceof CrafterBlock ||
                block instanceof DispenserBlock ||
                block instanceof DoorBlock ||
                block instanceof FenceGateBlock ||
                block instanceof FluidBlock ||
                block instanceof FrostedIceBlock ||
                block instanceof HopperBlock ||
                block instanceof NoteBlock ||
                block instanceof PistonBlock ||
                block instanceof PistonHeadBlock ||
                block instanceof RedstoneLampBlock ||
                block instanceof RedstoneTorchBlock ||
                block instanceof RedstoneWireBlock ||
                block instanceof SpongeBlock ||
                block instanceof StructureBlock ||
                block instanceof TntBlock ||
                block instanceof TrapdoorBlock;
    }
}
