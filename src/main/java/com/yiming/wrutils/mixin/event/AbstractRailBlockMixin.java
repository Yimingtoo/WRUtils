package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import com.yiming.wrutils.data.event.NeighborChangedEvent;
import com.yiming.wrutils.data.event.SimpleEvent;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin {
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateMixinHead(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        System.out.println("AbstractRailBlockMixin: sourceBlockPos is "+BaseEvent.entrySourcePos + "\tSize: "+BaseEvent.BLOCK_POS_STACK.size()+"\t"+ (!BaseEvent.BLOCK_POS_STACK.isEmpty()?BaseEvent.BLOCK_POS_STACK.peek():"null"));
        BaseEvent.BLOCK_POS_STACK.push(pos);
//        System.out.println("BaseEvent.BLOCK_POS_STACK.push At Rail");

        BlockPos sourcePos = BaseEvent.getFirstFromTop();
        Wrutils.eventRecorder.addEvent(new SimpleEvent(world.getTime(), MicroTimingSequence.BE, pos,sourcePos, SimpleEvent.EventType.NEIGHBOR_CHANGED));
    }
    @Inject(method = "neighborUpdate", at = @At("RETURN"))
    public void neighborUpdateMixinReturn(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        BaseEvent.BLOCK_POS_STACK.pop();
//        System.out.println("BaseEvent.BLOCK_POS_STACK.pop At Rail");

    }
}
