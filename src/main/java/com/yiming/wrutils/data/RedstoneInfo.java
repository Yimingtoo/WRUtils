package com.yiming.wrutils.data;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class RedstoneInfo {

    private double tick;
    private Block sourceBlock;
    private BlockPos sourcePos;
    private Block targetBlock;
    private BlockPos targetPos;
    private int delay;
    private int power;

    private String updateType; // NC PP S

    public RedstoneInfo(double tick, Block sourceBlock, BlockPos sourcePos, Block targetBlock, BlockPos targetPos) {
        this.tick = tick;
        this.sourceBlock = sourceBlock;
        this.sourcePos = sourcePos;
        this.targetBlock = targetBlock;
        this.targetPos = targetPos;
    }
}
