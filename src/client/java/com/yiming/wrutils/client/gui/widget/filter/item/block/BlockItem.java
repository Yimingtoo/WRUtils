package com.yiming.wrutils.client.gui.widget.filter.item.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class BlockItem implements FilterType<Block> {
    protected Block block;

    public BlockItem(Block block) {
        this.block = block;
    }

    abstract protected BlockState getEventState(BaseEvent event);

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


}
