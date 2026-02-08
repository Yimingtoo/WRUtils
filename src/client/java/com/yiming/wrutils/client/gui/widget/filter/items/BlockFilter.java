package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockFilter extends FilterTypeTemp {

    private final BlockFilterType blockType;

    public BlockFilter(BlockFilterType blockType) {
        this.blockType = blockType;
    }

    public BlockFilter updateFilter(Collection<Block> values) {
        Map<Block, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getBlock(), item.isChecked());
            }
        }
        this.clear();
        for (Block value : values) {
            Item item = new Item(this.blockType, value);
            CheckState checkState = oldMap.get(value);
            if (checkState != null) {
                item.setChecked(checkState);
            }
            this.addItem(item);
        }
        return this;
    }

    public static class Item extends FilterItem {
        private final BlockFilterType blockType;
        private final Block block;

        public Item(BlockFilterType blockType, Block block) {
            this.blockType = blockType;
            this.block = block;
        }

        protected BlockState getEventState(BaseEvent event) {
            return switch (this.blockType) {
                case BlockFilterType.SOURCE -> event.getSourceBlockInfo().state();
                case BlockFilterType.TARGET -> event.getTargetBlockInfo().state();
            };
        }

        public Block getBlock() {
            return this.block;
        }

        @Override
        public String getName() {
            return this.block.getName().getString();
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            BlockState state = this.getEventState(event);
            if (state != null) {
                return state.getBlock() == this.block;
            }
            return false;
        }
    }


}
