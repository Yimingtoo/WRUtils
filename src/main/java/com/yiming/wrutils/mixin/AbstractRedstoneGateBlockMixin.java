package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.UpdateInfo;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin {

    @Invoker("isLocked")
    public abstract boolean invokerIsLocked(WorldView world, BlockPos pos, BlockState state);

    @Invoker("hasPower")
    protected abstract boolean invokerHasPower(World world, BlockPos pos, BlockState state);


    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateMixinHead(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
//        System.out.println(" : " + this.invokerIsLocked(world, pos, state) + " : " + this.invokerHasPower(world, pos, state));
        if (state.canPlaceAt(world, pos)) {
            if (!this.invokerIsLocked(world, pos, state)) {
                boolean bl = (Boolean) state.get(Properties.POWERED);
                boolean bl2 = this.invokerHasPower(world, pos, state);
                if (bl != bl2 && !world.getBlockTickScheduler().isTicking(pos, (AbstractRedstoneGateBlock) (Object) this)) {
                    System.out.println("AbstractRedstoneGateBlockMixin: " + sourceBlock.toString());

                    System.out.println("AbstractRedstoneGateBlockMixin UpdateInfo: " + UpdateInfo.isBlockChecked(pos));
                    UpdateInfo.addCheckedBlockPos(new BlockPos(0, 0, 2));
                    UpdateInfo.removeCheckedBlockPos(new BlockPos(0, 0, 0));
                }
            }
        }

    }
}
