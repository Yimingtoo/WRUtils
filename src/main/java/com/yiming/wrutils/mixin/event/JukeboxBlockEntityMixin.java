package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.event.BlockInfo;
import com.yiming.wrutils.data.event.EventRecorder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {
    /**
     * Entry添加方块位置和状态信息
     * @Uncheck
     * TODO: 需要检查这个是否有必要，以及 pos 和 state 的是否正确
     */
    @Inject(method = "onManagerChange", at = @At("HEAD"))
    public void onManagerChange(CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.push(new BlockInfo(((JukeboxBlockEntity)(Object)this).getPos(), ((JukeboxBlockEntity)(Object)this).getCachedState()));
    }

    @Inject(method = "onManagerChange", at = @At("RETURN"))
    public void onManagerChange1(CallbackInfo ci) {
        EventRecorder.BLOCK_INFO_STACK.pop();

    }
}
