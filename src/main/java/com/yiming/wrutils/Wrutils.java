package com.yiming.wrutils;

import com.yiming.wrutils.block.AddedBlocks;
import com.yiming.wrutils.block.entity.ColoredBlockEntity;
import com.yiming.wrutils.block.entity.ModBlockEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;


public class Wrutils implements ModInitializer {
    public static final String MOD_ID = "wrutils-mod";

    public static final BlockEntityType<ColoredBlockEntity> COLORED_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            "wrutils:colored_block_entity",
            FabricBlockEntityTypeBuilder.create((BlockPos type, BlockState pos) -> new ColoredBlockEntity(type, pos)).build(null));

    @Override
    public void onInitialize() {
        AddedBlocks.registerModBlocks();
        ModBlockEntityTypes.initialize();
    }
}
