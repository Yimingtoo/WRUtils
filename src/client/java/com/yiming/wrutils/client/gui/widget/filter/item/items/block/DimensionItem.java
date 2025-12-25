package com.yiming.wrutils.client.gui.widget.filter.item.items.block;

import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.Dimension;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.ArrayList;

public class DimensionItem implements FilterType<Dimension> {
    Dimension dimension;
    protected BlockFilterType blockType;

    public DimensionItem(Dimension dimension, BlockFilterType blockType) {
        this.dimension = dimension;
        this.blockType = blockType;
    }

    protected Dimension getEventBlockDimension(BaseEvent event) {
        if (this.blockType == BlockFilterType.SOURCE) {
            return event.getSourceBlockInfo().dimension();
        } else if (this.blockType == BlockFilterType.TARGET) {
            return event.getTargetBlockInfo().dimension();
        }
        return null;
    }

    @Override
    public Dimension getValue() {
        return this.dimension;
    }

    @Override
    public String getName() {
        return this.dimension.getName();
    }

    @Override
    public void setValue(Dimension value) {
        this.dimension = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        return this.getEventBlockDimension(event) == this.dimension;
    }

    public static ArrayList<DimensionItem> getDimensionItems(BlockFilterType blockType) {
        ArrayList<DimensionItem> dimensionItems = new ArrayList<>();
        for (Dimension dimension : Dimension.values()) {
            if (dimension == Dimension.NONE) {
                continue;
            }
            dimensionItems.add(new DimensionItem(dimension, blockType));
        }
        return dimensionItems;
    }
}
