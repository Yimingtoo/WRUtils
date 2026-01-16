package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.base.LongItem;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameTickFilter extends FilterTypeTemp {
    public static long relativeTick = 0;
    private final OriginalTickItem originalTickItem = new OriginalTickItem(0);

    public static void setRelativeTick(long tick) {
        relativeTick = tick;
    }

    public OriginalTickItem getOriginalTickItem() {
        return this.originalTickItem;
    }

    public void setOriginalTickItem(long tick) {
        this.originalTickItem.setValue(tick);
    }

    @Override
    public void clear() {
        super.clear();
        this.originalTickItem.setValue(DataManager.getEventRecordFirstTick());
    }

    public static class Item extends FilterItem {
        private final TickData tickData;

        public Item(TickData tickData, CheckState checkState) {
            this.tickData = tickData;
            this.checkState = checkState;
        }

        public Item(long tick, CheckState checkState) {
            this.tickData = new TickData(tick);
            this.checkState = checkState;
        }

        public Item(long firstTick, long lastTick, CheckState checkState) {
            this.tickData = new TickData(firstTick, lastTick);
            this.checkState = checkState;
        }


        public void setTickData(TickData tickData) {
            this.tickData.of(tickData);
        }

        public void setTickData(long firstTick, long lastTick) {
            this.tickData.setTicks(firstTick, lastTick);
        }

        public void setTickData(long tick) {
            this.tickData.setTicks(tick);
        }

        @Override
        public void setValueFromText(String text) {
            boolean result = text.trim().matches("\\d+") || text.trim().matches("\\d+\\s*-\\s*\\d+");
            Pattern singleNumberPattern = Pattern.compile("\\d+");
            Pattern rangePattern = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)");
            if (result) {
                if (text.trim().isEmpty()) {
                    return;
                }
                text = text.trim();
                Matcher rangeMatcher = rangePattern.matcher(text);
                if (rangeMatcher.matches()) {
                    long first = Long.parseLong(rangeMatcher.group(1));
                    long second = Long.parseLong(rangeMatcher.group(2));
                    this.tickData.setTicks(first, second);
                }
                Matcher singleMatcher = singleNumberPattern.matcher(text);
                if (singleMatcher.matches()) {
                    long first = Long.parseLong(text);
                    this.tickData.setTicks(first);
                }
            }
        }

        @Override
        public String getName() {
            return this.tickData.toString();
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            long gameTime = event.getTimeStamp().gameTime();
            long firstTick = this.tickData.getFirstTick();
            long lastTick = this.tickData.getLastTick();
            if (firstTick == lastTick) {
                return gameTime == relativeTick + firstTick;
            } else {
                return gameTime >= relativeTick + firstTick && gameTime <= relativeTick + lastTick;
            }
        }
    }

    public static class OriginalTickItem extends LongItem {
        public OriginalTickItem(long value) {
            super(value);
        }

        @Override
        public void setValue(long value) {
            super.setValue(value);
            DataManagerClient.eventOriginTick = value;
        }

        @Override
        public void setValueFromText(String text) {
            boolean result = text.trim().matches("\\d+");
            Pattern singleNumberPattern = Pattern.compile("\\d+");
            if (result) {
                if (text.trim().isEmpty()) {
                    return;
                }
                text = text.trim();
                Matcher singleMatcher = singleNumberPattern.matcher(text);
                if (singleMatcher.matches()) {
                    long value = Long.parseLong(text);
                    this.setValue(value);
                }
            }
        }
    }


    public static class TickData {
        private long firstTick;
        private long lastTick;

        public TickData(long firstTick, long lastTick) {
            this.firstTick = firstTick;
            this.lastTick = lastTick;
        }

        public TickData(long tick) {
            this(tick, tick);
        }

        public void setTicks(long firstTick, long lastTick) {
            this.firstTick = firstTick;
            this.lastTick = lastTick;
        }

        public void setTicks(long tick) {
            this.firstTick = tick;
            this.lastTick = tick;
        }

        public void of(TickData tickData) {
            this.setTicks(tickData.getFirstTick(), tickData.getLastTick());
        }

        public long getFirstTick() {
            return this.firstTick;
        }

        public long getLastTick() {
            return this.lastTick;
        }

        @Override
        public String toString() {
            if (this.firstTick == this.lastTick) {
                return String.valueOf(this.firstTick);
            } else {
                return this.firstTick + " - " + this.lastTick;
            }
        }
    }
}
