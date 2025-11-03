package com.yiming.wrutils.client.render;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CubeSide {
    public Direction direction;
    public Vec3i pos1;
    public Vec3i pos2;
    public DrawStyle style;


    public CubeSide(Vec3i pos1, Vec3i pos2, DrawStyle style) {
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


    public static ArrayList<CubeSide> getSides(Vec3i pos1, Vec3i pos2, DrawStyle style) {

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

        ArrayList<CubeSide> cubeSides = new ArrayList<>();
        cubeSides.add(new CubeSide(p1, p2, style));
        cubeSides.add(new CubeSide(p2, p3, style));
        cubeSides.add(new CubeSide(p3, p4, style));
        cubeSides.add(new CubeSide(p4, p1, style));

        cubeSides.add(new CubeSide(p1, p5, style));
        cubeSides.add(new CubeSide(p2, p6, style));
        cubeSides.add(new CubeSide(p3, p7, style));
        cubeSides.add(new CubeSide(p4, p8, style));

        cubeSides.add(new CubeSide(p5, p6, style));
        cubeSides.add(new CubeSide(p6, p7, style));
        cubeSides.add(new CubeSide(p7, p8, style));
        cubeSides.add(new CubeSide(p8, p5, style));

        return cubeSides;
    }

    public static List<CubeSide> deduplicateSides(List<CubeSide> cubeSides, DrawStyle style) {
        Map<EdgeKey, CubeSide> map = new HashMap<>();

        for (CubeSide cubeSide : cubeSides) {
            EdgeKey key = new EdgeKey(cubeSide.pos1, cubeSide.pos2);
            if (map.containsKey(key)) {
                map.put(key, new CubeSide(key.a, key.b, style));
            } else {
                map.put(key, cubeSide);
            }
        }

        return new ArrayList<>(map.values());
    }

    public static List<CubeSide> cutSides(List<CubeSide> cubeSides, List<CubeSide> cubeSides1) {
        for (CubeSide cubeSide1 : cubeSides1) {
            List<CubeSide> nextCubeSides = new ArrayList<>();
            for (CubeSide cubeSide : cubeSides) {
                List<CubeSide> cut = cubeSide.cutSide(cubeSide1);
                if (!cut.isEmpty()) {
                    nextCubeSides.addAll(cut);
                }
            }
            cubeSides = nextCubeSides;
        }
        return cubeSides;
    }


    public boolean isSuperposition(CubeSide cubeSide) {
        return this.pos1.equals(cubeSide.pos1) && this.pos2.equals(cubeSide.pos2);
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

    public ArrayList<CubeSide> cutSide(CubeSide cubeSide) {
        ArrayList<CubeSide> cubeSides = new ArrayList<>();
        if (!isCollinear(cubeSide)) {
            cubeSides.add(this);
            return cubeSides;
        }
        int thisP1 = getPosAtAxis(1);
        int thisP2 = getPosAtAxis(2);
        int sideP1 = cubeSide.getPosAtAxis(1);
        int sideP2 = cubeSide.getPosAtAxis(2);
        if (thisP1 >= sideP1 && thisP2 <= sideP2) {
            return cubeSides;
        } else {
            if (thisP1 < sideP1) {
                cubeSides.add(new CubeSide(this.pos1, cubeSide.pos1, style));
            }
            if (thisP2 > sideP2) {
                cubeSides.add(new CubeSide(this.pos2, cubeSide.pos2, style));
            }
        }

        return cubeSides;
    }

    public boolean isCollinear(CubeSide cubeSide) {
        boolean isCollinear = false;
        switch (direction.getAxis()) {
            case Direction.Axis.X:
                if (pos1.getY() == cubeSide.pos1.getY() && pos2.getY() == cubeSide.pos2.getY() && pos1.getZ() == cubeSide.pos1.getZ() && pos2.getZ() == cubeSide.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Y:
                if (pos1.getX() == cubeSide.pos1.getX() && pos2.getX() == cubeSide.pos2.getX() && pos1.getZ() == cubeSide.pos1.getZ() && pos2.getZ() == cubeSide.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Z:
                if (pos1.getX() == cubeSide.pos1.getX() && pos2.getX() == cubeSide.pos2.getX() && pos1.getY() == cubeSide.pos1.getY() && pos2.getY() == cubeSide.pos2.getY()) {
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