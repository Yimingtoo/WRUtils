package net.minecraft.block.piston;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PistonHandler {
    public static final int MAX_MOVABLE_BLOCKS = 12;
    private final World world;
    private final BlockPos posFrom;
    private final boolean retracted;
    private final BlockPos posTo;
    private final Direction motionDirection;
    private final List<BlockPos> movedBlocks = Lists.<BlockPos>newArrayList();
    private final List<BlockPos> brokenBlocks = Lists.<BlockPos>newArrayList();
    private final Direction pistonDirection;

    public PistonHandler(World world, BlockPos pos, Direction dir, boolean retracted) {
        this.world = world;
        this.posFrom = pos;
        this.pistonDirection = dir;
        this.retracted = retracted;
        if (retracted) {
            this.motionDirection = dir;
            this.posTo = pos.offset(dir);
        } else {
            this.motionDirection = dir.getOpposite();
            this.posTo = pos.offset(dir, 2);
        }
    }

    /**
     * 计算活塞推动时需要移动或破坏的方块列表
     * 
     * @return 如果推动操作有效则返回true，否则返回false
     */
    public boolean calculatePush() {
        // 清空之前的移动和破坏方块记录
        this.movedBlocks.clear();
        this.brokenBlocks.clear();
        
        // 获取目标位置的方块状态
        BlockState blockState = this.world.getBlockState(this.posTo);
        
        // 检查目标位置的方块是否可以被活塞移动
        if (!PistonBlock.isMovable(blockState, this.world, this.posTo, this.motionDirection, false, this.pistonDirection)) {
            // 如果方块不可移动且活塞处于收缩状态，且方块具有DESTROY行为，则将其添加到破坏列表中
            if (this.retracted && blockState.getPistonBehavior() == PistonBehavior.DESTROY) {
                this.brokenBlocks.add(this.posTo);
                return true;
            } else {
                // 方块既不能移动也不能破坏，推动无效
                return false;
            }
            //posTo 为活塞Face的方块，不可能为自身方块
        } else if (!this.tryMove(this.posTo, this.motionDirection)) {
            // 尝试移动目标位置的方块失败，推动无效
            return false;
        } else {
            // 遍历所有已标记为移动的方块
            for (int i = 0; i < this.movedBlocks.size(); i++) {
                BlockPos blockPos = (BlockPos)this.movedBlocks.get(i);
                // 如果方块是粘性方块（如粘液块或蜂蜜块），则尝试移动与其相邻的粘性方块
                if (isBlockSticky(this.world.getBlockState(blockPos)) && !this.tryMoveAdjacentBlock(blockPos)) {
                    return false;
                }
            }

            // 所有检查通过，推动有效
            return true;
        }
    }

    /**
     * 判断方块是否为粘性方块（粘液块或蜂蜜块）
     * 
     * @param state 方块状态
     * @return 如果是粘性方块返回true，否则返回false
     */
    private static boolean isBlockSticky(BlockState state) {
        return state.isOf(Blocks.SLIME_BLOCK) || state.isOf(Blocks.HONEY_BLOCK);
    }

    /**
     * 判断两个相邻方块是否粘在一起
     * 粘液块和蜂蜜块之间不粘合，其他粘性方块与任意粘性方块都粘合
     * 
     * @param state 当前方块状态
     * @param adjacentState 相邻方块状态
     * @return 如果两个方块粘在一起返回true，否则返回false
     */
    private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
        // 粘液块和蜂蜜块之间不粘合
        if (state.isOf(Blocks.HONEY_BLOCK) && adjacentState.isOf(Blocks.SLIME_BLOCK)) {
            return false;
        } else if (state.isOf(Blocks.SLIME_BLOCK) && adjacentState.isOf(Blocks.HONEY_BLOCK)) {
            return false;
        } else {
            // 任意一个方块是粘性方块即可粘合
            return isBlockSticky(state) || isBlockSticky(adjacentState);
        }
    }

    /**
     * 尝试移动指定位置的方块及其相连的方块
     * 
     * @param pos 要移动的方块位置
     * @param dir 移动方向
     * @return 如果移动成功返回true，否则返回false
     */
    private boolean tryMove(BlockPos pos, Direction dir) {
        BlockState blockState = this.world.getBlockState(pos);
        
        // 空气方块无需移动
        if (blockState.isAir()) {
            return true;
        } 
        // 不可移动的方块直接返回true（不处理）
        else if (!PistonBlock.isMovable(blockState, this.world, pos, this.motionDirection, false, dir)) {
            return true;
        } 
        // 活塞本身位置无需移动
        else if (pos.equals(this.posFrom)) {
            return true;
        } 
        // 已经在移动列表中的方块无需重复处理
        else if (this.movedBlocks.contains(pos)) {
            return true;
        } 
        else {
            int i = 1;
            // 检查是否会超过最大移动方块数限制
            if (i + this.movedBlocks.size() > 12) {
                return false;
            } else {
                // 处理粘性方块的连锁移动：向后查找相连的粘性方块
                while (isBlockSticky(blockState)) {
                    BlockPos blockPos = pos.offset(this.motionDirection.getOpposite(), i);
                    BlockState blockState2 = blockState;
                    blockState = this.world.getBlockState(blockPos);
                    
                    // 如果遇到空气、不粘合的方块、不可移动方块或活塞位置则停止
                    if (blockState.isAir()
                            || !isAdjacentBlockStuck(blockState2, blockState)
                            || !PistonBlock.isMovable(blockState, this.world, blockPos, this.motionDirection, false, this.motionDirection.getOpposite())
                            || blockPos.equals(this.posFrom)) {
                        break;
                    }

                    // 检查是否会超过最大移动方块数限制
                    if (++i + this.movedBlocks.size() > 12) {
                        return false;
                    }
                }

                // 将向后找到的方块添加到移动列表中
                int j = 0;
                for (int k = i - 1; k >= 0; k--) {
                    this.movedBlocks.add(pos.offset(this.motionDirection.getOpposite(), k));
                    j++;
                }

                // 处理向前的方块移动：查找需要被推动的方块
                int k = 1;
                while (true) {
                    BlockPos blockPos2 = pos.offset(this.motionDirection, k);
                    int l = this.movedBlocks.indexOf(blockPos2);
                    
                    // 如果遇到已经在移动列表中的方块，重新排列移动顺序
                    if (l > -1) {
                        this.setMovedBlocks(j, l);

                        // 检查粘性方块的相邻方块是否也需要移动
                        for (int m = 0; m <= l + j; m++) {
                            BlockPos blockPos3 = (BlockPos)this.movedBlocks.get(m);
                            if (isBlockSticky(this.world.getBlockState(blockPos3)) && !this.tryMoveAdjacentBlock(blockPos3)) {
                                return false;
                            }
                        }

                        return true;
                    }

                    // 获取下一个位置的方块状态
                    blockState = this.world.getBlockState(blockPos2);
                    
                    // 空气方块，移动结束
                    if (blockState.isAir()) {
                        return true;
                    }

                    // 不可移动的方块或活塞本身位置，移动失败
                    if (!PistonBlock.isMovable(blockState, this.world, blockPos2, this.motionDirection, true, this.motionDirection) || blockPos2.equals(this.posFrom)) {
                        return false;
                    }

                    // 可破坏的方块，添加到破坏列表
                    if (blockState.getPistonBehavior() == PistonBehavior.DESTROY) {
                        this.brokenBlocks.add(blockPos2);
                        return true;
                    }

                    // 检查是否会超过最大移动方块数限制
                    if (this.movedBlocks.size() >= 12) {
                        return false;
                    }

                    // 添加到移动列表
                    this.movedBlocks.add(blockPos2);
                    j++;
                    k++;
                }
            }
        }
    }

    private void setMovedBlocks(int from, int to) {
        List<BlockPos> list = Lists.<BlockPos>newArrayList();
        List<BlockPos> list2 = Lists.<BlockPos>newArrayList();
        List<BlockPos> list3 = Lists.<BlockPos>newArrayList();
        list.addAll(this.movedBlocks.subList(0, to));
        list2.addAll(this.movedBlocks.subList(this.movedBlocks.size() - from, this.movedBlocks.size()));
        list3.addAll(this.movedBlocks.subList(to, this.movedBlocks.size() - from));
        this.movedBlocks.clear();
        this.movedBlocks.addAll(list);
        this.movedBlocks.addAll(list2);
        this.movedBlocks.addAll(list3);
    }

    private boolean tryMoveAdjacentBlock(BlockPos pos) {
        BlockState blockState = this.world.getBlockState(pos);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != this.motionDirection.getAxis()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState2 = this.world.getBlockState(blockPos);
                if (isAdjacentBlockStuck(blockState2, blockState) && !this.tryMove(blockPos, direction)) {
                    return false;
                }
            }
        }

        return true;
    }

    public Direction getMotionDirection() {
        return this.motionDirection;
    }

    public List<BlockPos> getMovedBlocks() {
        return this.movedBlocks;
    }

    public List<BlockPos> getBrokenBlocks() {
        return this.brokenBlocks;
    }
}
