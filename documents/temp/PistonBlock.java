package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class PistonBlock extends FacingBlock {
    public static final MapCodec<PistonBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(Codec.BOOL.fieldOf("sticky").forGetter(block -> block.sticky), createSettingsCodec()).apply(instance, PistonBlock::new)
    );
    public static final BooleanProperty EXTENDED = Properties.EXTENDED;
    public static final int field_31373 = 0;
    public static final int field_31374 = 1;
    public static final int field_31375 = 2;
    public static final float field_31376 = 4.0F;
    protected static final VoxelShape EXTENDED_EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);
    protected static final VoxelShape EXTENDED_WEST_SHAPE = Block.createCuboidShape(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EXTENDED_SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);
    protected static final VoxelShape EXTENDED_NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EXTENDED_UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    protected static final VoxelShape EXTENDED_DOWN_SHAPE = Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);
    private final boolean sticky;

    @Override
    public MapCodec<PistonBlock> getCodec() {
        return CODEC;
    }

    public PistonBlock(boolean sticky, AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(EXTENDED, Boolean.valueOf(false)));
        this.sticky = sticky;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if ((Boolean)state.get(EXTENDED)) {
            switch ((Direction)state.get(FACING)) {
                case DOWN:
                    return EXTENDED_DOWN_SHAPE;
                case UP:
                default:
                    return EXTENDED_UP_SHAPE;
                case NORTH:
                    return EXTENDED_NORTH_SHAPE;
                case SOUTH:
                    return EXTENDED_SOUTH_SHAPE;
                case WEST:
                    return EXTENDED_WEST_SHAPE;
                case EAST:
                    return EXTENDED_EAST_SHAPE;
            }
        } else {
            return VoxelShapes.fullCube();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            this.tryMove(world, pos, state);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!world.isClient) {
            this.tryMove(world, pos, state);
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (!world.isClient && world.getBlockEntity(pos) == null) {
                this.tryMove(world, pos, state);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()).with(EXTENDED, Boolean.valueOf(false));
    }

    private void tryMove(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        boolean bl = this.shouldExtend(world, pos, direction);
        if (bl && !(Boolean)state.get(EXTENDED)) {
            if (new PistonHandler(world, pos, direction, true).calculatePush()) {
                world.addSyncedBlockEvent(pos, this, 0, direction.getId());
            }
        } else if (!bl && (Boolean)state.get(EXTENDED)) {
            BlockPos blockPos = pos.offset(direction, 2);
            BlockState blockState = world.getBlockState(blockPos);
            int i = 1;
            if (blockState.isOf(Blocks.MOVING_PISTON)
                    && blockState.get(FACING) == direction
                    && world.getBlockEntity(blockPos) instanceof PistonBlockEntity pistonBlockEntity
                    && pistonBlockEntity.isExtending()
                    && (pistonBlockEntity.getProgress(0.0F) < 0.5F || world.getTime() == pistonBlockEntity.getSavedWorldTime() || ((ServerWorld)world).isInBlockTick())) {
                i = 2;
            }

            world.addSyncedBlockEvent(pos, this, i, direction.getId());
        }
    }

    private boolean shouldExtend(RedstoneView world, BlockPos pos, Direction pistonFace) {
        for (Direction direction : Direction.values()) {
            if (direction != pistonFace && world.isEmittingRedstonePower(pos.offset(direction), direction)) {
                return true;
            }
        }

        if (world.isEmittingRedstonePower(pos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockPos = pos.up();

            for (Direction direction2 : Direction.values()) {
                if (direction2 != Direction.DOWN && world.isEmittingRedstonePower(blockPos.offset(direction2), direction2)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    /**
     * 处理同步的方块事件，控制活塞的伸缩行为
     * 
     * @param state 活塞方块的状态
     * @param world 当前世界对象
     * @param pos 活塞方块的位置
     * @param type 事件类型：0表示伸出，1和2表示缩回
     * @param data 附加数据，包含方向信息
     * @return 是否成功处理事件
     */
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        Direction direction = state.get(FACING);
        BlockState blockState = state.with(EXTENDED, Boolean.valueOf(true));
        
        // 在服务端检查活塞是否应该伸展
        if (!world.isClient) {
            boolean bl = this.shouldExtend(world, pos, direction);
            // 如果活塞应该伸展但收到缩回指令，或者不应该伸展但收到伸出指令，则不处理
            if (bl && (type == 1 || type == 2)) {
                world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
                return false;
            }

            if (!bl && type == 0) {
                return false;
            }
        }

        // 处理活塞伸出事件
        if (type == 0) {
            // 尝试移动方块，如果失败则返回
            if (!this.move(world, pos, direction, true)) {
                return false;
            }

            // 更新活塞状态为已伸出
            world.setBlockState(pos, blockState, Block.NOTIFY_ALL | Block.MOVED);
            // 播放活塞伸出音效
            world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
            // 发送游戏事件
            world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Emitter.of(blockState));
        } 
        // 处理活塞缩回事件
        else if (type == 1 || type == 2) {
            // 获取并完成移动中的活塞方块实体
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));
            if (blockEntity instanceof PistonBlockEntity) {
                ((PistonBlockEntity)blockEntity).finish();
            }

            // 创建移动中的活塞方块状态
            BlockState blockState2 = Blocks.MOVING_PISTON
                    .getDefaultState()
                    .with(PistonExtensionBlock.FACING, direction)
                    .with(PistonExtensionBlock.TYPE, this.sticky ? PistonType.STICKY : PistonType.DEFAULT);
            
            // 设置移动中的活塞方块
            world.setBlockState(pos, blockState2, Block.NO_REDRAW | Block.FORCE_STATE);
            world.addBlockEntity(
                    PistonExtensionBlock.createBlockEntityPiston(pos, blockState2, this.getDefaultState().with(FACING, Direction.byId(data & 7)), direction, false, true)
            );
            
            // 更新邻居方块
            world.updateNeighbors(pos, blockState2.getBlock());
            blockState2.updateNeighbors(world, pos, Block.NOTIFY_LISTENERS);
            
            // 如果是粘性活塞，处理粘连方块
            if (this.sticky) {
                BlockPos blockPos = pos.add(direction.getOffsetX() * 2, direction.getOffsetY() * 2, direction.getOffsetZ() * 2);
                BlockState blockState3 = world.getBlockState(blockPos);
                boolean bl2 = false;
                
                // 检查是否为正在移动的活塞方块实体
                if (blockState3.isOf(Blocks.MOVING_PISTON)
                        && world.getBlockEntity(blockPos) instanceof PistonBlockEntity pistonBlockEntity
                        && pistonBlockEntity.getFacing() == direction
                        && pistonBlockEntity.isExtending()) {
                    pistonBlockEntity.finish();
                    bl2 = true;
                }

                // 根据条件决定是否移除或移动粘连的方块
                if (!bl2) {
                    if (type != 1
                            || blockState3.isAir()
                            || !isMovable(blockState3, world, blockPos, direction.getOpposite(), false, direction)
                            || blockState3.getPistonBehavior() != PistonBehavior.NORMAL && !blockState3.isOf(Blocks.PISTON) && !blockState3.isOf(Blocks.STICKY_PISTON)) {
                        world.removeBlock(pos.offset(direction), false);
                    } else {
                        this.move(world, pos, direction, false);
                    }
                }
            } else {
                // 非粘性活塞直接移除头部
                world.removeBlock(pos.offset(direction), false);
            }

            // 播放活塞缩回音效
            world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
            // 发送游戏事件
            world.emitGameEvent(GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Emitter.of(blockState2));
        }

        return true;
    }

    public static boolean isMovable(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir) {
        if (pos.getY() < world.getBottomY() || pos.getY() > world.getTopYInclusive() || !world.getWorldBorder().contains(pos)) {
            return false;
        } else if (state.isAir()) {
            return true;
        } else if (state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN) || state.isOf(Blocks.RESPAWN_ANCHOR) || state.isOf(Blocks.REINFORCED_DEEPSLATE)) {
            return false;
        } else if (direction == Direction.DOWN && pos.getY() == world.getBottomY()) {
            return false;
        } else if (direction == Direction.UP && pos.getY() == world.getTopYInclusive()) {
            return false;
        } else {
            if (!state.isOf(Blocks.PISTON) && !state.isOf(Blocks.STICKY_PISTON)) {
                if (state.getHardness(world, pos) == -1.0F) {
                    return false;
                }

                switch (state.getPistonBehavior()) {
                    case BLOCK:
                        return false;
                    case DESTROY:
                        return canBreak;
                    case PUSH_ONLY:
                        return direction == pistonDir;
                }
            } else if ((Boolean)state.get(EXTENDED)) {
                return false;
            }

            return !state.hasBlockEntity();
        }
    }


    /**
     * 移动活塞相关的方块
     * 
     * @param world 当前世界对象
     * @param pos 活塞方块的位置
     * @param dir 活塞面向的方向
     * @param extend 活塞状态：true为伸出，false为缩回
     * @return 是否成功执行移动操作
     */
    private boolean move(World world, BlockPos pos, Direction dir, boolean extend) {
        BlockPos blockPos = pos.offset(dir);
        // 如果是缩回操作且前方是活塞头，则将其移除
        if (!extend && world.getBlockState(blockPos).isOf(Blocks.PISTON_HEAD)) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NO_REDRAW | Block.FORCE_STATE);
        }

        // 创建活塞处理器来计算推动逻辑
        PistonHandler pistonHandler = new PistonHandler(world, pos, dir, extend);
        if (!pistonHandler.calculatePush()) {
            return false;
        } else {
            // 存储需要移动的方块及其状态
            Map<BlockPos, BlockState> map = Maps.<BlockPos, BlockState>newHashMap();
            List<BlockPos> list = pistonHandler.getMovedBlocks();      // 需要移动的方块位置列表
            List<BlockState> list2 = Lists.<BlockState>newArrayList(); // 对应的方块状态列表

            // 填充需要移动的方块信息
            for (BlockPos blockPos2 : list) {
                BlockState blockState = world.getBlockState(blockPos2);
                list2.add(blockState);
                map.put(blockPos2, blockState);
            }

            // 获取将被破坏的方块列表
            List<BlockPos> list3 = pistonHandler.getBrokenBlocks();
            BlockState[] blockStates = new BlockState[list.size() + list3.size()];
            // 确定移动方向：伸出时为活塞面向方向，缩回时为相反方向
            Direction direction = extend ? dir : dir.getOpposite();
            int i = 0;

            // 先处理将被破坏的方块（逆序处理）
            for (int j = list3.size() - 1; j >= 0; j--) {
                BlockPos blockPos3 = (BlockPos)list3.get(j);
                BlockState blockState2 = world.getBlockState(blockPos3);
                // 如果方块有方块实体，则获取它以便正确掉落物品
                BlockEntity blockEntity = blockState2.hasBlockEntity() ? world.getBlockEntity(blockPos3) : null;
                // 掉落方块中的物品
                dropStacks(blockState2, world, blockPos3, blockEntity);
                // 将方块设置为空气
                world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                // 发送方块被破坏的游戏事件
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos3, GameEvent.Emitter.of(blockState2));
                // 添加破坏粒子效果（火焰方块除外）
                if (!blockState2.isIn(BlockTags.FIRE)) {
                    world.addBlockBreakParticles(blockPos3, blockState2);
                }

                blockStates[i++] = blockState2;
            }

            // 处理需要移动的方块（逆序处理）
            for (int j = list.size() - 1; j >= 0; j--) {
                BlockPos blockPos3 = (BlockPos)list.get(j);
                BlockState blockState2 = world.getBlockState(blockPos3);
                // 计算移动后的新位置
                blockPos3 = blockPos3.offset(direction);
                // 从待处理映射中移除新位置
                map.remove(blockPos3);
                // 创建移动中的活塞方块状态
                BlockState blockState3 = Blocks.MOVING_PISTON.getDefaultState().with(FACING, dir);
                // 设置移动中的活塞方块
                world.setBlockState(blockPos3, blockState3, Block.NO_REDRAW | Block.MOVED);
                // 添加活塞扩展方块实体
                world.addBlockEntity(PistonExtensionBlock.createBlockEntityPiston(blockPos3, blockState3, (BlockState)list2.get(j), dir, extend, false));
                blockStates[i++] = blockState2;
            }

            // 如果是伸出操作，添加活塞头
            if (extend) {
                PistonType pistonType = this.sticky ? PistonType.STICKY : PistonType.DEFAULT;
                // 创建活塞头方块状态
                BlockState blockState4 = Blocks.PISTON_HEAD.getDefaultState().with(PistonHeadBlock.FACING, dir).with(PistonHeadBlock.TYPE, pistonType);
                // 创建移动中的活塞方块状态
                BlockState blockState2 = Blocks.MOVING_PISTON
                        .getDefaultState()
                        .with(PistonExtensionBlock.FACING, dir)
                        .with(PistonExtensionBlock.TYPE, this.sticky ? PistonType.STICKY : PistonType.DEFAULT);
                // 从待处理映射中移除活塞位置
                map.remove(blockPos);
                // 设置活塞位置的移动中方块
                world.setBlockState(blockPos, blockState2, Block.NO_REDRAW | Block.MOVED);
                // 添加活塞扩展方块实体
                world.addBlockEntity(PistonExtensionBlock.createBlockEntityPiston(blockPos, blockState2, blockState4, dir, true, true));
            }

            // 清理剩余的方块（这些方块已被移动，原位置应变为空气）
            BlockState blockState5 = Blocks.AIR.getDefaultState();

            for (BlockPos blockPos4 : map.keySet()) {
                world.setBlockState(blockPos4, blockState5, Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.MOVED);
            }

            // 更新邻居方块状态
            for (Entry<BlockPos, BlockState> entry : map.entrySet()) {
                BlockPos blockPos5 = (BlockPos)entry.getKey();
                BlockState blockState6 = (BlockState)entry.getValue();
                blockState6.prepare(world, blockPos5, 2);
                blockState5.updateNeighbors(world, blockPos5, Block.NOTIFY_LISTENERS);
                blockState5.prepare(world, blockPos5, 2);
            }

            // 获取红石信号方向
            WireOrientation wireOrientation = OrientationHelper.getEmissionOrientation(world, pistonHandler.getMotionDirection(), null);
            i = 0;

            // 更新被破坏方块的邻居
            for (int k = list3.size() - 1; k >= 0; k--) {
                BlockState blockState3 = blockStates[i++];
                BlockPos blockPos6 = (BlockPos)list3.get(k);
                blockState3.prepare(world, blockPos6, 2);
                world.updateNeighborsAlways(blockPos6, blockState3.getBlock(), wireOrientation);
            }

            // 更新移动方块的邻居
            for (int k = list.size() - 1; k >= 0; k--) {
                world.updateNeighborsAlways((BlockPos)list.get(k), blockStates[i++].getBlock(), wireOrientation);
            }

            // 如果是伸出操作，更新活塞头的邻居
            if (extend) {
                world.updateNeighborsAlways(blockPos, Blocks.PISTON_HEAD, wireOrientation);
            }

            return true;
        }
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return (Boolean)state.get(EXTENDED);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
