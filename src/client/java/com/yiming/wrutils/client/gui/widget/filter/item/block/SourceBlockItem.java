package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.HashSet;

public class SourceBlockItem extends BlockItem {
    public SourceBlockItem(Block block) {
        super(block);
    }

    @Override
    protected BlockState getEventState(BaseEvent event) {
        return event.getSourceBlockInfo().state();
    }



    public static ArrayList<SourceBlockItem> getBlockItems(HashSet<Block> blockSet) {
        ArrayList<SourceBlockItem> blockItems = new ArrayList<>();
        for (Block block : blockSet) {
            blockItems.add(new SourceBlockItem(block));
        }
        return blockItems;
    }
}
