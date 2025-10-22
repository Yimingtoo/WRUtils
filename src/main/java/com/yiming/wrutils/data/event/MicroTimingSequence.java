package com.yiming.wrutils.data.event;

public enum MicroTimingSequence {
    WTU("World Time Update"),
    NTE("Next Tick Entry"),
    RAID("Raid"),
    RT("Random Tick"),
    BE("Block Event"),
    EU("Entity Update"),
    TE("Tile Entity"),
    NU("Network Update");

    private String name;

    MicroTimingSequence(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
