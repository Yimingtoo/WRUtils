package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameTickFilter implements FilterTypeTemp {
        public static long relativeTick = 0;
//    public Long relativeTick = 0L;
    private final List<Item> items = new ArrayList<>();


    @Override
    public List<Item> getItems() {
        return this.items;
    }

    public static class Item implements FilterItem {
        private final TickData tickData;
        private boolean isChecked = false;

        public Item(TickData tickData, boolean isChecked) {
            this.tickData = tickData;
            this.isChecked = isChecked;
        }

        public Item(long tick, boolean isChecked) {
            this.tickData = new TickData(tick);
            this.isChecked = isChecked;
        }

        public Item(long firstTick, long lastTick, boolean isChecked) {
            this.tickData = new TickData(firstTick, lastTick);
            this.isChecked = isChecked;
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
        public boolean isChecked() {
            return isChecked;
        }

        @Override
        public void setChecked(boolean checked) {
            this.isChecked = checked;
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
