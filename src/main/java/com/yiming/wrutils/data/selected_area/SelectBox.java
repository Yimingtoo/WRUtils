package com.yiming.wrutils.data.selected_area;

import net.minecraft.util.math.Vec3i;

public class SelectBox {
    private String selectBoxName = "Sub-Area-1";
    Vec3i pos1;
    Vec3i pos2;

    boolean isVisible;
    boolean isUsed;


    public SelectBox(Vec3i pos1, Vec3i pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Vec3i pos1() {
        return pos1;
    }

    public Vec3i pos2() {
        return pos2;
    }

    public void setPos1(Vec3i pos) {
        pos1 = pos;
    }

    public void setPos2(Vec3i pos) {
        pos2 = pos;
    }

    public String getName() {
        return selectBoxName;
    }

    public void setName(String name) {
        this.selectBoxName = name;
    }

}
