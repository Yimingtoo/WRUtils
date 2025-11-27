package com.yiming.wrutils.data.event;

import java.util.ArrayList;

@Deprecated
public class EventRecorder {

    //    public static ModItemEntity modItemEntity11 = null;


    ArrayList<BaseEvent> eventList = new ArrayList<>();

    public void add(BaseEvent event) {
        eventList.add(event);
    }

    public void clearEvents() {
        eventList.clear();
    }

    public BaseEvent getEvent(int index) {
        return eventList.get(index);
    }

    public BaseEvent getLast() {
        return eventList.getLast();
    }

    public void printEvents() {
        long lastGameTime = -1;
        for (BaseEvent event : eventList) {
            if (lastGameTime != -1 && event.getTimeStamp().gameTime() != lastGameTime) {
                System.out.println();
            }
            System.out.println(event.toString());

            lastGameTime = event.getTimeStamp().gameTime();
        }
    }


    public int eventsSize() {
        return eventList.size();
    }


}
