package com.yiming.wrutils.client.gui.widget.filter.item;

public class GameTickItem implements FilterType {
    long firstTick;
    long lastTick;

    public GameTickItem(long firstTick, long lastTick) {
        this.firstTick = firstTick;
        this.lastTick = lastTick;
    }

    public GameTickItem(long tick) {
        this(tick, tick);
    }

    public long getFirstTick() {
        return firstTick;
    }

    public long getLastTick() {
        return lastTick;
    }


    @Override
    public String getName() {
        if (this.firstTick == this.lastTick) {
            return String.valueOf(this.firstTick);
        } else {
            return this.firstTick + " - " + this.lastTick;
        }
    }
}
