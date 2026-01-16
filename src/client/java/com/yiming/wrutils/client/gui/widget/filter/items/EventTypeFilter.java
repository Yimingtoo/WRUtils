package com.yiming.wrutils.client.gui.widget.filter.items;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.EventType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventTypeFilter extends FilterTypeTemp {
    public EventTypeFilter() {
        super();
        for (EventType eventType : EventType.values()) {
            this.addItem(new Item(eventType));
        }

    }

    public FilterTypeTemp updateFilter(Collection<EventType> values) {
        Map<EventType, CheckState> oldMap = new HashMap<>();
        for (FilterItem item : this.items) {
            Item item1 = (Item) item;
            oldMap.put(item1.getEventType(), item.isChecked());
        }
        this.clear();
        for (EventType value : values) {
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
        private final EventType eventType;

        public Item(EventType type) {
            this.eventType = type;
        }

        public EventType getEventType() {
            return this.eventType;
        }

        @Override
        public String getName() {
            return this.eventType.getName();
        }

        @Override
        public boolean collectOrNotByItem(BaseEvent event) {
            return event.getEventType() == this.eventType;

        }
    }
}
