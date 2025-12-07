package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameTickItem implements FilterType {
    private long firstTick;
    private long lastTick;

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

    public void setValue(long firstTick, long lastTick) {
        this.firstTick = firstTick;
        this.lastTick = lastTick;
    }

    public void setValue(long tick) {
        this.setValue(tick, tick);
    }

    @Override
    public void setData(String text) {
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
                this.setValue(first, second);
            }
            Matcher singleMatcher = singleNumberPattern.matcher(text);
            if (singleMatcher.matches()) {
                long first = Long.parseLong(text);
                this.setValue(first);
            }
        }
    }


    @Override
    public String getName() {
        if (this.firstTick == this.lastTick) {
            return String.valueOf(this.firstTick);
        } else {
            return this.firstTick + " - " + this.lastTick;
        }
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        long gameTime = event.getTimeStamp().gameTime();
        if (firstTick == lastTick) {
            return gameTime == DataManager.eventOriginTick + firstTick;
        } else {
            return gameTime >= DataManager.eventOriginTick + firstTick && gameTime <= DataManager.eventOriginTick + lastTick;
        }
    }



}
