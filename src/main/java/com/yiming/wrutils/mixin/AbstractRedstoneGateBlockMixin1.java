package com.yiming.wrutils.mixin;

import com.yiming.wrutils.data.UpdateInfo;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated
@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin1 {

    @Invoker("isLocked")
    public abstract boolean invokerIsLocked(WorldView world, BlockPos pos, BlockState state);

    @Invoker("hasPower")
    protected abstract boolean invokerHasPower(World world, BlockPos pos, BlockState state);

    @Invoker("isTargetNotAligned")
    public abstract boolean invokerIsTargetNotAligned(BlockView world, BlockPos pos, BlockState state);
    @Invoker("getUpdateDelayInternal")
    protected abstract int invokerGetUpdateDelayInternal(BlockState var1);


    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateMixinHead(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
//        System.out.println(" : " + this.invokerIsLocked(world, pos, state) + " : " + this.invokerHasPower(world, pos, state));
        UpdateInfo updateInfo = new UpdateInfo(world.getTime());


        if (state.canPlaceAt(world, pos)) {
            if (!this.invokerIsLocked(world, pos, state)) {
                boolean bl = (Boolean) state.get(Properties.POWERED);
                boolean bl2 = this.invokerHasPower(world, pos, state);
                if (bl != bl2 && !world.getBlockTickScheduler().isTicking(pos, (AbstractRedstoneGateBlock) (Object) this)) {
                    System.out.println("AbstractRedstoneGateBlockMixin: " + sourceBlock.toString());
                    TickPriority tickPriority = TickPriority.HIGH;
                    if (this.invokerIsTargetNotAligned(world, pos, state)) {
                        tickPriority = TickPriority.EXTREMELY_HIGH;
                    } else if (bl) {
                        tickPriority = TickPriority.VERY_HIGH;
                    }
                    if (world.getBlockTickScheduler().isQueued(pos, (AbstractRedstoneGateBlock) (Object) this)) {
                        String description = ("Fail info: Schedule tick is already added!");
                        updateInfo.setTickScheduleInfo(false, tickPriority, this.invokerGetUpdateDelayInternal(state), description);
                    } else {
                        String description = ("Success info: The repeater adds scheduleBlockTick!");
                        updateInfo.setTickScheduleInfo(true, tickPriority, this.invokerGetUpdateDelayInternal(state), description);
                    }
                } else {
                    String description = "Fail info: ";
                    if (bl == bl2) {
                        description += "The POWER of the repeater is already " + (bl ? "" : "not ") + "Powered! ";
                    }
                    if (world.getBlockTickScheduler().isTicking(pos, (AbstractRedstoneGateBlock) (Object) this)) {
                        description += "Repeater is Ticking!";
                    }
                    updateInfo.setTickScheduleInfo(false, null, -1, description);

                }
            } else {
                String description = ("Fail info: Repeater is LOCKED!");
                updateInfo.setTickScheduleInfo(false, null, -1, description);
            }
        } else {
            String description = ("Fail info: state.canPlaceAt(world, pos) is FALSE!");
            updateInfo.setTickScheduleInfo(false, null, -1, description);
        }
//        Wrutils.redstoneInfoManager.addUpdateInfoUnit(updateInfo, pos);

    }
}
