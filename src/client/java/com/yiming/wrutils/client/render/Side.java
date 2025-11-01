package com.yiming.wrutils.client.render;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Side {
    public Direction direction;
    public Vec3i pos1;
    public Vec3i pos2;
    public DrawStyle style;


    public Side(Vec3i pos1, Vec3i pos2, DrawStyle style) {
        direction = Direction.getFacing(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY(), pos2.getZ() - pos1.getZ());
        if (direction.getVector().getX() < 0 || direction.getVector().getY() < 0 || direction.getVector().getZ() < 0) {
            this.pos1 = pos2;
            this.pos2 = pos1;
            this.direction = direction.getOpposite();
        } else {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        this.style = style;

    }



    public static ArrayList<Side> getSides(Vec3i pos1, Vec3i pos2, DrawStyle style) {

        int dx = (pos2.getX() - pos1.getX());
        int dy = (pos2.getY() - pos1.getY());
        int dz = (pos2.getZ() - pos1.getZ());

        Vec3i p1 = pos1;
        Vec3i p2 = pos1.add(new Vec3i(dx, 0, 0));
        Vec3i p3 = pos1.add(new Vec3i(dx, 0, dz));
        Vec3i p4 = pos1.add(new Vec3i(0, 0, dz));
        Vec3i p5 = pos1.add(new Vec3i(0, dy, 0));
        Vec3i p6 = pos1.add(new Vec3i(dx, dy, 0));
        Vec3i p7 = pos1.add(new Vec3i(dx, dy, dz));
        Vec3i p8 = pos1.add(new Vec3i(0, dy, dz));

        //       p7────────────p8
        //      /│            /│
        //     / │           / │
        //   p6────────────v5  │
        //    │  │          │  │
        //    │  p3─────────│──p4           j  k
        //    │ /           │ /             │ /
        //    │/            │/              │/
        //   p2────────────p1       i ── ── O

        ArrayList<Side> sides = new ArrayList<>();
        sides.add(new Side(p1, p2, style));
        sides.add(new Side(p2, p3, style));
        sides.add(new Side(p3, p4, style));
        sides.add(new Side(p4, p1, style));

        sides.add(new Side(p1, p5, style));
        sides.add(new Side(p2, p6, style));
        sides.add(new Side(p3, p7, style));
        sides.add(new Side(p4, p8, style));

        sides.add(new Side(p5, p6, style));
        sides.add(new Side(p6, p7, style));
        sides.add(new Side(p7, p8, style));
        sides.add(new Side(p8, p5, style));

        return sides;
    }

    public static List<Side> deduplicateSides(List<Side> sides, DrawStyle style) {
        Map<EdgeKey, Side> map = new HashMap<>();

        for (Side side : sides) {
            EdgeKey key = new EdgeKey(side.pos1, side.pos2);
            if (map.containsKey(key)) {
                // 重复边 → 标记为 RED
                map.put(key, new Side(key.a, key.b, style));
            } else {
                map.put(key, side);
            }
        }

        return new ArrayList<>(map.values());
    }


    public boolean isSuperposition(Side side) {
        return this.pos1.equals(side.pos1) && this.pos2.equals(side.pos2);
    }


    public int getPosAtAxis(int i) {
        int out = 0;
        switch (this.direction.getAxis()) {
            case Direction.Axis.X:
                out = (i == 1 ? pos1.getX() : pos2.getX());
                break;
            case Direction.Axis.Y:
                out = (i == 1 ? pos1.getY() : pos2.getY());
                break;
            case Direction.Axis.Z:
                out = (i == 1 ? pos1.getZ() : pos2.getZ());
                break;
        }
        return out;
    }

    public ArrayList<Side> cutSide(Side side) {
        ArrayList<Side> sides = new ArrayList<>();
        if (!isCollinear(side)) {
            sides.add(this);
            return sides;
        }
        int thisP1 = getPosAtAxis(1);
        int thisP2 = getPosAtAxis(2);
        int sideP1 = side.getPosAtAxis(1);
        int sideP2 = side.getPosAtAxis(2);
        if (thisP1 >= sideP1 && thisP2 <= sideP2) {
            return sides;
        } else {
            if (thisP1 < sideP1) {
                sides.add(new Side(this.pos1, side.pos1, style));
            }
            if (thisP2 > sideP2) {
                sides.add(new Side(this.pos2, side.pos2, style));
            }
        }

        return sides;
    }

    public boolean isCollinear(Side side) {
        boolean isCollinear = false;
        switch (direction.getAxis()) {
            case Direction.Axis.X:
                if (pos1.getY() == side.pos1.getY() && pos2.getY() == side.pos2.getY() && pos1.getZ() == side.pos1.getZ() && pos2.getZ() == side.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Y:
                if (pos1.getX() == side.pos1.getX() && pos2.getX() == side.pos2.getX() && pos1.getZ() == side.pos1.getZ() && pos2.getZ() == side.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Z:
                if (pos1.getX() == side.pos1.getX() && pos2.getX() == side.pos2.getX() && pos1.getY() == side.pos1.getY() && pos2.getY() == side.pos2.getY()) {
                    isCollinear = true;
                }
                break;
        }
        return isCollinear;
    }

    @Override
    public int hashCode() {
        return pos1.hashCode() + pos2.hashCode();
    }


    record EdgeKey(Vec3i a, Vec3i b) {
        EdgeKey {
            if (a.compareTo(b) > 0) {
                Vec3i tmp = a;
                a = b;
                b = tmp;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EdgeKey other)) return false;
            return a.equals(other.a) && b.equals(other.b);
        }

        @Override
        public int hashCode() {
            return a.hashCode() + b.hashCode();
        }
    }


}