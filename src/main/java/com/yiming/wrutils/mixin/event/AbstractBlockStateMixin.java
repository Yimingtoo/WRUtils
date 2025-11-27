package com.yiming.wrutils.mixin.event;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @see net.minecraft.block.AbstractBlock
 */
//@Mixin(targets = "net/minecraft/block/AbstractBlock$AbstractBlockState")
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Invoker("asBlockState")
    protected abstract BlockState asBlockStateMixin();

    /**
     * @see net.minecraft.block.AbstractBlock.AbstractBlockState#neighborUpdate(World, BlockPos, Block, WireOrientation, boolean)
     */
    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdate(World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, this.asBlockStateMixin()));

        if (!world.isClient()) {
            if (isBlockNeededForNeighborUpdate(((AbstractBlock.AbstractBlockState) (Object) this).getBlock())) {
                DataManager.eventRecorder.add(
                        new NeighborChangedEvent(
                                DataManager.currentGameTime,
                                DataManager.currentMicroTimingSequence,
                                new BlockInfo(pos, this.asBlockStateMixin()),
                                DataManager.entrySourceBlockInfo,
                                EventType.NEIGHBOR_CHANGED));
            }
        }
    }

    @Inject(method = "neighborUpdate", at = @At("RETURN"))
    public void neighborUpdate1(World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();
    }

    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"))
    public void getStateForNeighborUpdate(WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random, CallbackInfoReturnable<BlockState> cir) {

        if (!world.isClient()) {
            if (isBlockNeededForPostPlacement(((AbstractBlock.AbstractBlockState) (Object) this).getBlock())) {
                DataManager.eventRecorder.add(
                        new SimpleEvent(
                                DataManager.currentGameTime,
                                DataManager.currentMicroTimingSequence,
                                new BlockInfo(pos, this.asBlockStateMixin()),
                                DataManager.entrySourceBlockInfo,
                                EventType.POST_PLACEMENT));
            }
        }

    }


    @Inject(method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("HEAD"))
    public void updateNeighbors(WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.push(new BlockInfo(pos, world.getBlockState(pos)));
//        System.out.println("asdffffffffffffffffffffafdsfasd" +  world.getBlockState(pos).getBlock());;

    }

    @Inject(method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("RETURN"))
    public void updateNeighbors1(WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth, CallbackInfo ci) {
        DataManager.BLOCK_INFO_STACK.pop();

    }

    @Unique
    private boolean isBlockNeededForNeighborUpdate(Block block) {
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

    @Unique
    private boolean isBlockNeededForPostPlacement(Block block) {
        return block instanceof AbstractCoralBlock ||
                block instanceof AbstractPlantBlock ||
                block instanceof AbstractPlantStemBlock ||
                block instanceof AbstractPressurePlateBlock ||
                block instanceof AbstractRailBlock ||
                block instanceof AbstractSignBlock ||
                block instanceof AbstractTorchBlock ||
                block instanceof AmethystClusterBlock ||
                block instanceof AttachedStemBlock ||
                block instanceof BambooBlock ||
                block instanceof BambooShootBlock ||
                block instanceof BannerBlock ||
                block instanceof BarrierBlock ||
                block instanceof BedBlock ||
                block instanceof BeehiveBlock ||
                block instanceof BellBlock ||
                block instanceof BigDripleafBlock ||
                block instanceof BigDripleafStemBlock ||
                block instanceof BrushableBlock ||
                block instanceof BubbleColumnBlock ||
                block instanceof CactusBlock ||
                block instanceof CakeBlock ||
                block instanceof CampfireBlock ||
                block instanceof CandleBlock ||
                block instanceof CandleCakeBlock ||
                block instanceof CarpetBlock ||
                block instanceof ChainBlock ||
                block instanceof ChestBlock ||
                block instanceof ChorusFlowerBlock ||
                block instanceof ChorusPlantBlock ||
                block instanceof CocoaBlock ||
                block instanceof ComparatorBlock ||
                block instanceof ConcretePowderBlock ||
                block instanceof ConduitBlock ||
                block instanceof CoralBlock ||
                block instanceof CoralBlockBlock ||
                block instanceof CoralFanBlock ||
                block instanceof CoralWallFanBlock ||
                block instanceof CreakingHeartBlock ||
                block instanceof DeadCoralWallFanBlock ||
                block instanceof DecoratedPotBlock ||
                block instanceof DirtPathBlock ||
                block instanceof DoorBlock ||
                block instanceof EnderChestBlock ||
                block instanceof FallingBlock ||
                block instanceof FarmlandBlock ||
                block instanceof FenceBlock ||
                block instanceof FenceGateBlock ||
                block instanceof FireBlock ||
                block instanceof FlowerPotBlock ||
                block instanceof FluidBlock ||
                block instanceof FrogspawnBlock ||
                block instanceof GrateBlock ||
                block instanceof HangingMossBlock ||
                block instanceof HangingRootsBlock ||
                block instanceof HangingSignBlock ||
                block instanceof HeavyCoreBlock ||
                block instanceof LadderBlock ||
                block instanceof LanternBlock ||
                block instanceof LeavesBlock ||
                block instanceof LightBlock ||
                block instanceof LightningRodBlock ||
                block instanceof MagmaBlock ||
                block instanceof MangroveRootsBlock ||
                block instanceof MultifaceBlock ||
                block instanceof MushroomBlock ||
                block instanceof NetherPortalBlock ||
                block instanceof NoteBlock ||
                block instanceof ObserverBlock ||
                block instanceof PaleMossCarpetBlock ||
                block instanceof PaneBlock ||
                block instanceof PistonHeadBlock ||
                block instanceof PitcherCropBlock ||
                block instanceof PlantBlock ||
                block instanceof PointedDripstoneBlock ||
                block instanceof PropaguleBlock ||
                block instanceof RedstoneWireBlock ||
                block instanceof RepeaterBlock ||
                block instanceof ScaffoldingBlock ||
                block instanceof SculkSensorBlock ||
                block instanceof SculkShriekerBlock ||
                block instanceof SeagrassBlock ||
                block instanceof SeaPickleBlock ||
                block instanceof SignBlock ||
                block instanceof SlabBlock ||
                block instanceof SmallDripleafBlock ||
                block instanceof SnowBlock ||
                block instanceof SnowyBlock ||
                block instanceof SoulFireBlock ||
                block instanceof SoulSandBlock ||
                block instanceof SporeBlossomBlock ||
                block instanceof StairsBlock ||
                block instanceof SugarCaneBlock ||
                block instanceof TallPlantBlock ||
                block instanceof TrapdoorBlock ||
                block instanceof TripwireBlock ||
                block instanceof TripwireHookBlock ||
                block instanceof VineBlock ||
                block instanceof WallBannerBlock ||
                block instanceof WallBlock ||
                block instanceof WallHangingSignBlock ||
                block instanceof WallMountedBlock ||
                block instanceof WallRedstoneTorchBlock ||
                block instanceof WallSignBlock ||
                block instanceof WallTorchBlock;
    }
}
