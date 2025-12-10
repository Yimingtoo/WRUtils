package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.HashSet;

public class TargetBlockItem extends BlockItem {
    public TargetBlockItem(Block block) {
        super(block);
    }

    @Override
    protected BlockState getEventState(BaseEvent event) {
        return event.getTargetBlockInfo().state();
    }

    public static ArrayList<TargetBlockItem> getBlockItems(HashSet<Block> blockSet) {
        ArrayList<TargetBlockItem> blockItems = new ArrayList<>();
        for (Block block : blockSet) {
            blockItems.add(new TargetBlockItem(block));
        }
        return blockItems;
    }
}
