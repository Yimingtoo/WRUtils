package com.yiming.wrutils.block;

import com.yiming.wrutils.Wrutils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Deprecated
public class AddedBlocks {
    public static final Block COLORED_BLOCK = registerBlock("colored_block", Block::new, Block.Settings.create());

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, name, block);
    }

    private static Block registerBlock(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of("tutorial", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
//        Items.register(block);
        return block;
    }

    public static void initializeAndRegister() {
        System.out.println("Registering Blocks for " + Wrutils.MOD_ID);
    }
}
