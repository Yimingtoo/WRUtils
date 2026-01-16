package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SequenceFilter extends FilterTypeTemp {
    public SequenceFilter() {
        super();
        for (MicroTimingSequence sequence : MicroTimingSequence.values()) {
            this.addItem(new Item(sequence));
        }
    }

    public SequenceFilter updateFilter(Collection<MicroTimingSequence> values) {
        Map<MicroTimingSequence, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            if (item instanceof Item item1) {
                oldMap.put(item1.getSequence(), item.isChecked());
            }
        }
        this.clear();
        for (MicroTimingSequence value : values) {
            Item item = new Item(value);
            CheckState checkState = oldMap.get(value);
            if (checkState != null) {
                item.setChecked(checkState);
            }
            this.addItem(item);
        }
        return this;
    }

    public static class Item extends FilterItem {
        private final MicroTimingSequence sequence;

        public Item(MicroTimingSequence sequence) {
            this.sequence = sequence;
        }

        public MicroTimingSequence getSequence() {
            return sequence;
        }

        @Override
        public String getName() {
            return this.sequence.getAbbr();
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            return event.getTimeStamp().sequence() == this.sequence;
        }
    }


}
