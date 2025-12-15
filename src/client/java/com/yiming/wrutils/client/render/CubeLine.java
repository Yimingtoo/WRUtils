package com.yiming.wrutils.client.render;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeLine {
    public Direction direction;
    public Vec3i pos1;
    public Vec3i pos2;
    public WrutilsColor style;


    public CubeLine(Vec3i pos1, Vec3i pos2, WrutilsColor style) {
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


    public static ArrayList<CubeLine> getLines(Vec3i pos1, Vec3i pos2, WrutilsColor style) {

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

        ArrayList<CubeLine> cubeLines = new ArrayList<>();
        cubeLines.add(new CubeLine(p1, p2, style));
        cubeLines.add(new CubeLine(p2, p3, style));
        cubeLines.add(new CubeLine(p3, p4, style));
        cubeLines.add(new CubeLine(p4, p1, style));

        cubeLines.add(new CubeLine(p1, p5, style));
        cubeLines.add(new CubeLine(p2, p6, style));
        cubeLines.add(new CubeLine(p3, p7, style));
        cubeLines.add(new CubeLine(p4, p8, style));

        cubeLines.add(new CubeLine(p5, p6, style));
        cubeLines.add(new CubeLine(p6, p7, style));
        cubeLines.add(new CubeLine(p7, p8, style));
        cubeLines.add(new CubeLine(p8, p5, style));

        return cubeLines;
    }

    public static List<CubeLine> deduplicateLines(List<CubeLine> cubeLines, WrutilsColor style) {
        Map<EdgeKey, CubeLine> map = new HashMap<>();

        for (CubeLine cubeLine : cubeLines) {
            EdgeKey key = new EdgeKey(cubeLine.pos1, cubeLine.pos2);
            if (map.containsKey(key)) {
                map.put(key, new CubeLine(key.a, key.b, style));
            } else {
                map.put(key, cubeLine);
            }
        }

        return new ArrayList<>(map.values());
    }

    public static List<CubeLine> cutLines(List<CubeLine> cubeLines, List<CubeLine> cubeLines1) {
        for (CubeLine cubeLine1 : cubeLines1) {
            List<CubeLine> nextCubeLines = new ArrayList<>();
            for (CubeLine cubeLine : cubeLines) {
                List<CubeLine> cut = cubeLine.cutSide(cubeLine1);
                if (!cut.isEmpty()) {
                    nextCubeLines.addAll(cut);
                }
            }
            cubeLines = nextCubeLines;
        }
        return cubeLines;
    }


    public boolean isSuperposition(CubeLine cubeLine) {
        return this.pos1.equals(cubeLine.pos1) && this.pos2.equals(cubeLine.pos2);
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

    public ArrayList<CubeLine> cutSide(CubeLine cubeLine) {
        ArrayList<CubeLine> cubeLines = new ArrayList<>();
        if (!isCollinear(cubeLine)) {
            cubeLines.add(this);
            return cubeLines;
        }
        int thisP1 = getPosAtAxis(1);
        int thisP2 = getPosAtAxis(2);
        int sideP1 = cubeLine.getPosAtAxis(1);
        int sideP2 = cubeLine.getPosAtAxis(2);
        if (thisP1 >= sideP1 && thisP2 <= sideP2) {
            return cubeLines;
        } else {
            if (thisP1 < sideP1) {
                cubeLines.add(new CubeLine(this.pos1, cubeLine.pos1, style));
            }
            if (thisP2 > sideP2) {
                cubeLines.add(new CubeLine(this.pos2, cubeLine.pos2, style));
            }
        }

        return cubeLines;
    }

    public boolean isCollinear(CubeLine cubeLine) {
        boolean isCollinear = false;
        switch (direction.getAxis()) {
            case Direction.Axis.X:
                if (pos1.getY() == cubeLine.pos1.getY() && pos2.getY() == cubeLine.pos2.getY() && pos1.getZ() == cubeLine.pos1.getZ() && pos2.getZ() == cubeLine.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Y:
                if (pos1.getX() == cubeLine.pos1.getX() && pos2.getX() == cubeLine.pos2.getX() && pos1.getZ() == cubeLine.pos1.getZ() && pos2.getZ() == cubeLine.pos2.getZ()) {
                    isCollinear = true;
                }
                break;
            case Direction.Axis.Z:
                if (pos1.getX() == cubeLine.pos1.getX() && pos2.getX() == cubeLine.pos2.getX() && pos1.getY() == cubeLine.pos1.getY() && pos2.getY() == cubeLine.pos2.getY()) {
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