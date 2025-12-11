package com.yiming.wrutils.client.gui.widget.filter.item;

import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.EventType;

import java.util.ArrayList;

public class EventTypeItem implements FilterType<EventType> {
    private EventType eventType;

    public EventTypeItem(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public EventType getValue() {
        return this.eventType;
    }

    @Override
    public String getName() {
        return eventType.getName();
    }

    @Override
    public void setValue(EventType value) {
        this.eventType = value;
    }

    @Override
    public boolean collectOrNot(BaseEvent event) {
        return event.getEventType() == this.eventType;
    }

    public static ArrayList<EventTypeItem> EventTypes() {
        ArrayList<EventTypeItem> list = new ArrayList<>();
        for (EventType eventType : EventType.values()) {
            list.add(new EventTypeItem(eventType));
        }
        return list;
    }
}
