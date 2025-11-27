package com.yiming.wrutils.data.selected_area;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class SelectBox {
    private String selectBoxName = "Sub-Area-1";
    private Vec3i pos1;
    private Vec3i pos2;
    private Vec3i moveCtrlPos;
    SelectedCorner inertanceCorner;

    private Vec3i minPos;
    private Vec3i maxPos;


    boolean isVisible;
    boolean isUsed;


    public SelectBox(Vec3i pos1, Vec3i pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.setMinMaxPos();
        this.inertanceCorner = SelectedCorner.CORNER_1;

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
        this.inertanceCorner = SelectedCorner.CORNER_1;

    }

    public void setPos2(Vec3i pos) {
        pos2 = pos;
        setMinMaxPos();
        this.inertanceCorner = SelectedCorner.CORNER_2;
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

    public void setMoveCtrlPos(Vec3i pos) {
        this.moveCtrlPos = pos;
    }

    public Vec3i getMoveCtrlPos() {
        return moveCtrlPos;
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

    public void moveBox(Entity cameraEntity, int amount) {
        if (cameraEntity == null) return;
        Vec3d rotationVector = cameraEntity.getRotationVector();
        Direction facing = Direction.getFacing(rotationVector);
        Vec3i moveDist = facing.getVector().multiply(amount);
        if (this.moveCtrlPos == null) {
            this.pos1 = this.pos1.add(moveDist);
            this.pos2 = this.pos2.add(moveDist);
            this.setMinMaxPos();
        } else {
            switch (this.getBeMovingCorner(facing)) {
                case SelectedCorner.CORNER_1:
                    this.setPos1(this.pos1.add(moveDist));
                    break;
                case SelectedCorner.CORNER_2:
                    this.setPos2(this.pos2.add(moveDist));
                    break;
            }
            this.moveCtrlPos = this.moveCtrlPos.add(moveDist);
        }

    }

    private SelectedCorner getBeMovingCorner(Direction facing) {
        Direction.Axis axis = facing.getAxis();
        boolean result1 = this.pos1.getComponentAlongAxis(axis) == this.moveCtrlPos.getComponentAlongAxis(axis);
        boolean result2 = this.pos2.getComponentAlongAxis(axis) == this.moveCtrlPos.getComponentAlongAxis(axis);
        if (result1 && !result2) {
            this.inertanceCorner = SelectedCorner.CORNER_1;
            return SelectedCorner.CORNER_1;
        } else if (!result1 && result2) {
            this.inertanceCorner = SelectedCorner.CORNER_2;
            return SelectedCorner.CORNER_2;
        } else if (result1 && result2) {
            return this.inertanceCorner;
        }
        return this.inertanceCorner;
    }

    public enum SelectedCorner {
        CORNER_1,
        CORNER_2,
    }


    public enum CornerDirection {
        WEST_NORTH_DOWN(false, false, false, new Vec3i(-1, -1, -1)),
        WEST_NORTH_UP__(false, false, true, new Vec3i(-1, -1, 1)),
        WEST_SOUTH_DOWN(false, true, false, new Vec3i(-1, 1, -1)),
        WEST_SOUTH_UP__(false, true, true, new Vec3i(-1, 1, 1)),
        EAST_NORTH_DOWN(true, false, false, new Vec3i(1, -1, -1)),
        EAST_NORTH_UP__(true, false, true, new Vec3i(1, -1, 1)),
        EAST_SOUTH_DOWN(true, true, false, new Vec3i(1, 1, -1)),
        EAST_SOUTH_UP__(true, true, true, new Vec3i(1, 1, 1));

        private final Vec3i dir;
        private final boolean xMax;
        private final boolean yMax;
        private final boolean zMax;

        private CornerDirection(boolean xMax, boolean yMax, boolean zMax, Vec3i dir) {
            this.xMax = xMax;
            this.yMax = yMax;
            this.zMax = zMax;
            this.dir = dir;
        }

        public Vec3i getDir() {
            return this.dir;
        }

        public boolean isXMax() {
            return this.xMax;
        }

        public boolean isYMax() {
            return this.yMax;
        }

        public boolean isZMax() {
            return this.zMax;
        }

        public static Vec3i getPosWithDir(Vec3i minPos, Vec3i maxPos, CornerDirection dir) {
            return new Vec3i(
                    dir.isXMax() ? maxPos.getX() : minPos.getX(),
                    dir.isYMax() ? maxPos.getY() : minPos.getY(),
                    dir.isZMax() ? maxPos.getZ() : minPos.getZ()
            );
        }

        public static ArrayList<Vec3i> getCornerCubePoses(SelectBox selectBox) {
            Vec3i minPos = selectBox.minPos;
            Vec3i maxPos = selectBox.maxPos;
            ArrayList<Vec3i> poses = new ArrayList<>();
            for (CornerDirection dir : CornerDirection.values()) {
                poses.add(getPosWithDir(minPos, maxPos, dir));
            }
            return poses;
        }

    }

}
