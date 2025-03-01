package com.yiming.wrutils.block.entity;

import com.yiming.wrutils.block.AddedBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("wrutils-entity", path), blockEntityType);
    }

    public static final BlockEntityType<ColoredBlockEntity> COLORED_BLOCK = register(
            "colored_block",
            FabricBlockEntityTypeBuilder.create(ColoredBlockEntity::new, AddedBlocks.COLORED_BLOCK).build()
    );

    public static void initialize() {
    }
}
