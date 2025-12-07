package com.yiming.wrutils.client.gui.widget.filter.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongItem implements FilterType {
    protected long value;

    public LongItem(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public static ArrayList<LongItem> getIntegerItems(Collection<Long> values) {
        ArrayList<LongItem> items = new ArrayList<LongItem>();
        for (Long value : values) {
            items.add(new LongItem(value));
        }
        return items;
    }

    @Override
    public String getName() {
        return String.valueOf(value);
    }

    @Override
    public void setData(String text) {
        boolean result = text.trim().matches("\\d+");
        Pattern singleNumberPattern = Pattern.compile("\\d+");
        if (result) {
            if (text.trim().isEmpty()) {
                return;
            }
            text = text.trim();

            Matcher singleMatcher = singleNumberPattern.matcher(text);
            if (singleMatcher.matches()) {
                long first = Long.parseLong(text);
                this.setValue(first);

            }
        }
    }

}
