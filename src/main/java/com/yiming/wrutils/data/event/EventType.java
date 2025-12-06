package com.yiming.wrutils.data.event;

public enum EventType {
    UNKNOWN("Unknow"),
    NEIGHBOR_CHANGED("NC Update"),
    POST_PLACEMENT("PP Update"),
    SCHEDULED_TICK_ADD("Tick Add"),
    SCHEDULED_TICK_EXEC("Tick Exec");

    String name;

    EventType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
