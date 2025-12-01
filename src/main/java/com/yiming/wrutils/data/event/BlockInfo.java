package com.yiming.wrutils.data.event;

import com.yiming.wrutils.data.Dimension;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.rmi.registry.Registry;

public record BlockInfo(BlockPos pos, Dimension dimension, @Nullable BlockState state) {
    public BlockInfo(BlockPos pos, World world, BlockState state) {
        this(pos, Dimension.getWorldDimension(world), state);
    }

    public BlockInfo(BlockPos pos, BlockState state) {
        this(pos, Dimension.NONE, state);
    }

    @Override
    public String toString() {

        return String.format("<%s>{%d, %d, %d}", state.getBlock() != null ? state.getBlock().getName().getString() : "null", pos.getX(), pos.getY(), pos.getZ());
    }
}
