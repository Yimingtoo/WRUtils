package com.yiming.wrutils.block.entity;

import com.yiming.wrutils.block.AddedBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

@Deprecated
public class ColoredBlockEntity extends BlockEntity {

    public ColoredBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COLORED_BLOCK, pos, state);
    }

    private int color = 0xADD8E6; // 淡蓝色的RGB值

    public int getColor() {
        return color;
    }
}
