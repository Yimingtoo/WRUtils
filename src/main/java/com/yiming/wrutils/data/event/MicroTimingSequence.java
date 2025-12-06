package com.yiming.wrutils.data.event;

public enum MicroTimingSequence {
    WTU("WTU", "World Time Update"),
    NTE("NTE", "Next Tick Entry"),
    RAID("RAID", "Raid"),
    RT("RT", "Random Tick"),
    BE("BE", "Block Event"),
    EU("EU", "Entity Update"),
    TE("TE", "Tile Entity"),
    NU("NU", "Network Update");

    private final String name;
    private final String abbr;

    MicroTimingSequence(String abbr, String name) {
        this.name = name;
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }
}
