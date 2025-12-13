package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.HashSet;

public class BlockItem implements FilterType<Block> {

    protected Block block;
    protected BlockFilterType blockType;

    public BlockItem(Block block, BlockFilterType blockType) {
        this.block = block;
        this.blockType = blockType;
    }

    protected BlockState getEventState(BaseEvent event) {
        if (this.blockType == BlockFilterType.SOURCE) {
            return event.getSourceBlockInfo().state();
        } else if (this.blockType == BlockFilterType.TARGET) {
            return event.getTargetBlockInfo().state();
        }
        return null;
    }

    @Override
    public Block getValue() {
        return this.block;
    }

    @Override
    public String getName() {
        return this.block.getName().getString();
    }

    @Override
    public void setValue(Block value) {
        this.block = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        BlockState state = this.getEventState(event);
        if (state != null) {
            return state.getBlock() == this.block;
        }
        return false;
    }

    public static ArrayList<BlockItem> getBlockItems(HashSet<Block> blockSet, BlockFilterType blockType) {
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        for (Block block : blockSet) {
            blockItems.add(new BlockItem(block, blockType));
        }
        return blockItems;
    }


}
