package com.yiming.wrutils.client.render;

import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeFaces {
    private Vec3i posMin;
    private double expand = 0.002;

    public Map<Direction, CubeFace> faces = new HashMap<>();

    public CubeFaces(Vec3i pos1, Vec3i pos2, WrutilsColor style) {
        int x_min = 0;
        int y_min = 0;
        int z_min = 0;

        int x_max = Math.abs(pos2.getX() - pos1.getX()) + 1;
        int y_max = Math.abs(pos2.getY() - pos1.getY()) + 1;
        int z_max = Math.abs(pos2.getZ() - pos1.getZ()) + 1;
        posMin = new Vec3i(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));

        faces.put(Direction.DOWN, new CubeFace(new Vec3i(x_min, y_min, z_min), new Vec3i(x_max, y_min, z_max), style));
        faces.put(Direction.UP, new CubeFace(new Vec3i(x_min, y_max, z_min), new Vec3i(x_max, y_max, z_max), style));
        faces.put(Direction.NORTH, new CubeFace(new Vec3i(x_min, y_min, z_min), new Vec3i(x_max, y_max, z_min), style));
        faces.put(Direction.SOUTH, new CubeFace(new Vec3i(x_min, y_min, z_max), new Vec3i(x_max, y_max, z_max), style));
        faces.put(Direction.WEST, new CubeFace(new Vec3i(x_min, y_min, z_min), new Vec3i(x_min, y_max, z_max), style));
        faces.put(Direction.EAST, new CubeFace(new Vec3i(x_max, y_min, z_min), new Vec3i(x_max, y_max, z_max), style));

    }

    public void setExpand(double expand) {
        this.expand = expand;
    }

    public Vec3i getPosMin() {
        return posMin;
    }

    public void setAllStyle(WrutilsColor style) {
        // 遍历faces的每个元素
        for (Map.Entry<Direction, CubeFace> entry : faces.entrySet()) {
            CubeFace cubeFace = entry.getValue();
            cubeFace.style = style;
        }
    }

    public void setStyle(Direction direction, WrutilsColor style) {
        CubeFace cubeFace = faces.get(direction);
        if (cubeFace != null) {
            cubeFace.style = style;
        }
    }


    public class CubeFace {
        private final Vec3i pos1;
        private final Vec3i pos2;
        public WrutilsColor style;

        public CubeFace(Vec3i pos1, Vec3i pos2, WrutilsColor style) {
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.style = style;
        }


        public List<Vec3i> getPoints(Direction direction) {
            return switch (direction.getAxis()) {
                case Direction.Axis.X ->
                        List.of(pos1, new Vec3i(pos1.getX(), pos2.getY(), pos1.getZ()), pos2, new Vec3i(pos1.getX(), pos1.getY(), pos2.getZ()));
                case Direction.Axis.Y ->
                        List.of(pos1, new Vec3i(pos1.getX(), pos1.getY(), pos2.getZ()), pos2, new Vec3i(pos2.getX(), pos1.getY(), pos1.getZ()));
                case Direction.Axis.Z ->
                        List.of(pos1, new Vec3i(pos1.getX(), pos2.getY(), pos1.getZ()), pos2, new Vec3i(pos2.getX(), pos1.getY(), pos1.getZ()));
            };
        }

        public List<Vec3d> getExpandPoints(Direction direction) {
            Vec3d dir = new Vec3d(direction.getVector()).multiply(expand);
            return switch (direction.getAxis()) {
                case Direction.Axis.X -> List.of(
                        new Vec3d(pos1).add(dir),
                        new Vec3d(pos1.getX(), pos2.getY(), pos1.getZ()).add(dir),
                        new Vec3d(pos2).add(dir),
                        new Vec3d(pos1.getX(), pos1.getY(), pos2.getZ()).add(dir)
                );
                case Direction.Axis.Y -> List.of(
                        new Vec3d(pos1).add(dir),
                        new Vec3d(pos1.getX(), pos1.getY(), pos2.getZ()).add(dir),
                        new Vec3d(pos2).add(dir),
                        new Vec3d(pos2.getX(), pos1.getY(), pos1.getZ()).add(dir)
                );
                case Direction.Axis.Z -> List.of(
                        new Vec3d(pos1).add(dir),
                        new Vec3d(pos1.getX(), pos2.getY(), pos1.getZ()).add(dir),
                        new Vec3d(pos2).add(dir),
                        new Vec3d(pos2.getX(), pos1.getY(), pos1.getZ()).add(dir)
                );
            };
        }

    }


}
