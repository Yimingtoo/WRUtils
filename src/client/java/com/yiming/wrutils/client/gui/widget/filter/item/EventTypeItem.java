package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.EventType;

import java.util.ArrayList;

public class EventTypeItem implements FilterType {
    private EventType eventType;

    public EventTypeItem(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String getName() {
        return eventType.getName();
    }

    public static ArrayList<EventTypeItem> EventTypes() {
        ArrayList<EventTypeItem> list = new ArrayList<>();
        for (EventType eventType : EventType.values()) {
            list.add(new EventTypeItem(eventType));
        }
        return list;
    }
}
