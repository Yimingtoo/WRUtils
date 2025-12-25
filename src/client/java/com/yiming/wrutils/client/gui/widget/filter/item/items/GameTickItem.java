package com.yiming.wrutils.client.gui.widget.filter.item.items;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameTickItem implements FilterType<GameTickItem.TickData> {
    private TickData tickData;

    public GameTickItem(TickData tickData) {
        this.tickData = tickData;
    }

    public GameTickItem(long firstTick, long lastTick) {
        this.tickData = new TickData(firstTick, lastTick);
    }

    public GameTickItem(long tick) {
        this.tickData = new TickData(tick);
    }

    @Override
    public TickData getValue() {
        return this.tickData;
    }

    @Override
    public String getName() {
        return this.tickData.getName();
    }

    @Override
    public void setValue(TickData value) {
        this.tickData = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        long gameTime = event.getTimeStamp().gameTime();
        long firstTick = this.tickData.getFirstTick();
        long lastTick = this.tickData.getLastTick();
        if (firstTick == lastTick) {
            return gameTime == DataManagerClient.eventOriginTick + firstTick;
        } else {
            return gameTime >= DataManagerClient.eventOriginTick + firstTick && gameTime <= DataManagerClient.eventOriginTick + lastTick;
        }
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

        public long getFirstTick() {
            return this.firstTick;
        }

        public long getLastTick() {
            return this.lastTick;
        }

        public String getName() {
            if (this.firstTick == this.lastTick) {
                return String.valueOf(this.firstTick);
            } else {
                return this.firstTick + " - " + this.lastTick;
            }
        }
    }
}
