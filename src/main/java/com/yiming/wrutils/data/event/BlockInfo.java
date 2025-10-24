package com.yiming.wrutils.data.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public record BlockInfo(BlockPos pos, @Nullable BlockState state) {
    @Override
    public String toString() {

        return String.format("<%s>{%d, %d, %d}", state.getBlock() != null ? state.getBlock().getName().getString() : "null", pos.getX(), pos.getY(), pos.getZ());
    }
}
