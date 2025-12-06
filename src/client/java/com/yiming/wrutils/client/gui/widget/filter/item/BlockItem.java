package com.yiming.wrutils.client.gui.widget.filter.item;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashSet;

public class BlockItem implements FilterType {
    private Block block;

    public BlockItem(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String getName() {
        return this.block.getName().getString();
    }

    public static ArrayList<BlockItem> getBlockItems(HashSet<Block> blockSet) {
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        for (Block block : blockSet) {
            blockItems.add(new BlockItem(block));
        }
        return blockItems;
    }
}
