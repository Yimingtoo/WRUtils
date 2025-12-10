package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import com.yiming.wrutils.data.event.ScheduledTickInfo;

import java.util.ArrayList;

public class SequenceItem implements FilterType<MicroTimingSequence> {
    private MicroTimingSequence sequence;

    public SequenceItem(MicroTimingSequence sequence) {
        this.sequence = sequence;
    }

    public MicroTimingSequence getSequence() {
        return this.sequence;
    }

    @Override
    public MicroTimingSequence getValue() {
        return this.sequence;
    }

    @Override
    public String getName() {
        return this.sequence.getAbbr();
    }

    @Override
    public void setValue(MicroTimingSequence value) {
        this.sequence = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        return event.getTimeStamp().sequence() == this.sequence;
    }

    public static ArrayList<SequenceItem> SequenceItems() {
        ArrayList<SequenceItem> list = new ArrayList<SequenceItem>();
        for (MicroTimingSequence sequence : MicroTimingSequence.values()) {
            list.add(new SequenceItem(sequence));
        }
        return list;
    }


}
