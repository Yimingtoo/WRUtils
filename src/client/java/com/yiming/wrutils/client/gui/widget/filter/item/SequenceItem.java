package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.MicroTimingSequence;

import java.util.ArrayList;

public class SequenceItem implements FilterType {
    private MicroTimingSequence sequence;

    public SequenceItem(MicroTimingSequence sequence) {
        this.sequence = sequence;
    }

    public MicroTimingSequence getSequence() {
        return this.sequence;
    }

    @Override
    public String getName() {
        return this.sequence.getAbbr();
    }

    public static ArrayList<SequenceItem> SequenceItems() {
        ArrayList<SequenceItem> list = new ArrayList<SequenceItem>();
        for (MicroTimingSequence sequence : MicroTimingSequence.values()) {
            list.add(new SequenceItem(sequence));
        }
        return list;
    }
}
