package com.yiming.wrutils.data.select_box;

import net.minecraft.util.math.Vec3i;

public class SelectBox {
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
}
