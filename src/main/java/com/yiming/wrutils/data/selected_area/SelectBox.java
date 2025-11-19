package com.yiming.wrutils.data.selected_area;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SelectBox {
    private String selectBoxName = "Sub-Area-1";
    private Vec3i pos1;
    private Vec3i pos2;

    private Vec3i minPos;
    private Vec3i maxPos;


    boolean isVisible;
    boolean isUsed;


    public SelectBox(Vec3i pos1, Vec3i pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.setMinMaxPos();
    }

    private void setMinMaxPos() {
        this.minPos = new Vec3i(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        this.maxPos = new Vec3i(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

    public Vec3i pos1() {
        return pos1;
    }

    public Vec3i pos2() {
        return pos2;
    }

    public void setPos1(Vec3i pos) {
        pos1 = pos;
        setMinMaxPos();
    }

    public void setPos2(Vec3i pos) {
        pos2 = pos;
        setMinMaxPos();
    }

    public String getName() {
        return selectBoxName;
    }

    public void setName(String name) {
        this.selectBoxName = name;
    }

    public Vec3d getMinBorderPos() {
        return Vec3d.of(minPos);
    }

    public Vec3d getMaxBorderPos() {
        return Vec3d.of(maxPos.add(1, 1, 1));
    }


    public boolean isContainVec3iPos(Vec3i pos) {
        return pos.getX() >= minPos.getX() && pos.getX() <= maxPos.getX()
                && pos.getY() >= minPos.getY() && pos.getY() <= maxPos.getY()
                && pos.getZ() >= minPos.getZ() && pos.getZ() <= maxPos.getZ();
    }

    public boolean isContainVec3dPos(Vec3d pos) {
        return pos.getX() >= minPos.getX() && pos.getX() <= maxPos.getX() + 1
                && pos.getY() >= minPos.getY() && pos.getY() <= maxPos.getY() + 1
                && pos.getZ() >= minPos.getZ() && pos.getZ() <= maxPos.getZ() + 1;
    }

//    public List<>

}
