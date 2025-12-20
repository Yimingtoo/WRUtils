package com.yiming.wrutils.client.utils;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class GeometryUtil {
    /**
     * 计算直线与矩形在指定平面上的交点距离
     *
     * @param linePoint   直线起点
     * @param lineDir     直线方向向量
     * @param plane       平面
     * @param maxDistance 最大考虑距离
     * @return 如果存在有效交点且在maxDistance内，返回交点距离；
     * 如果没有有效交点，返回-1；
     * 如果矩形未正确对齐到指定平面，返回-2
     */
    public static double intersectLineWithRectangle(Vec3d linePoint, Vec3d lineDir, Plane plane, double maxDistance) {
        Vec3d posMin = plane.posMin;
        Vec3d posMax = plane.posMax;
        Direction.Axis planeDir = plane.planeDir;

        // 检查矩形是否为平面（即在指定轴上厚度为0）
        if (Math.abs(posMax.getComponentAlongAxis(planeDir) - posMin.getComponentAlongAxis(planeDir)) > 1e-9) {
            System.out.println("Warning: Rectangle is not a plane");
            return -2;
        }

        // 如果直线与平面平行，则无交点
        if (Math.abs(lineDir.getComponentAlongAxis(planeDir)) < 1e-9) {
            return -1;
        }

        // 计算直线与平面相交的参数t
        double t = (posMin.getComponentAlongAxis(planeDir) - linePoint.getComponentAlongAxis(planeDir)) / lineDir.getComponentAlongAxis(planeDir);

        // 如果交点在起点之后，则无效
        if (t < 0) {
            return -1;
        }

        // 计算实际距离并与最大距离比较
        double dist = lineDir.length() * t;
        if (dist > maxDistance) {
            return -1;
        }

        // 计算交点坐标
        double x = planeDir == Direction.Axis.X ? posMin.x : linePoint.x + t * lineDir.x;
        double y = planeDir == Direction.Axis.Y ? posMin.y : linePoint.y + t * lineDir.y;
        double z = planeDir == Direction.Axis.Z ? posMin.z : linePoint.z + t * lineDir.z;

        // 检查交点是否在矩形范围内
        boolean inX = (planeDir == Direction.Axis.X) || (x >= posMin.x && x <= posMax.x);
        boolean inY = (planeDir == Direction.Axis.Y) || (y >= posMin.y && y <= posMax.y);
        boolean inZ = (planeDir == Direction.Axis.Z) || (z >= posMin.z && z <= posMax.z);
        if (inX && inY && inZ) {
            return dist;
        }

        return -1;
    }


    /**
     * 根据摄像机位置和两个点定义的包围盒，获取面向摄像机的平面
     *
     * @param camPos 摄像机位置
     * @param pos1   包围盒的第一个角点
     * @param pos2   包围盒的第二个角点
     * @return 面向摄像机的平面列表
     */
    public static ArrayList<Plane> getPlanes(Vec3d camPos, Vec3d pos1, Vec3d pos2) {
        ArrayList<Plane> planes = new ArrayList<>();
        Vec3d minPos = new Vec3d(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z));
        Vec3d maxPos = new Vec3d(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y), Math.max(pos1.z, pos2.z));
        for (Direction.Axis axis : Direction.Axis.values()) {
            double camCoord = camPos.getComponentAlongAxis(axis);
            double minCoord = minPos.getComponentAlongAxis(axis);
            double maxCoord = maxPos.getComponentAlongAxis(axis);

            // 如果摄像机在包围盒的某一侧，则添加对应朝向的平面
            if (camCoord > maxCoord) {
                planes.add(new Plane(pos1, pos2, maxCoord, axis));
            } else if (camCoord < minCoord) {
                planes.add(new Plane(pos1, pos2, minCoord, axis));
            }
        }
        return planes;
    }

    public static double intersectLineWithBox(Vec3d camPos, Vec3d camDir, Vec3d pos1, Vec3d pos2) {
        ArrayList<GeometryUtil.Plane> planes = GeometryUtil.getPlanes(camPos, pos1, pos2);
        for (GeometryUtil.Plane plane : planes) {
            double distance1 = GeometryUtil.intersectLineWithRectangle(camPos, camDir, plane, 128);
            if (distance1 > 0) {
                return distance1;
            }
        }
        return -1;
    }


    public static class Plane {
        public Vec3d posMin;
        public Vec3d posMax;
        public Direction.Axis planeDir;


        /**
         * 构造一个平面矩形
         *
         * @param pos1     第一个点
         * @param pos2     第二个点
         * @param planePos 平面上的坐标值（根据planeDir确定在哪一轴上）
         * @param planeDir 平面的法线方向轴
         */
        public Plane(Vec3d pos1, Vec3d pos2, double planePos, Direction.Axis planeDir) {
            this.planeDir = planeDir;
            switch (planeDir) {
                case Direction.Axis.X:
                    this.posMin = new Vec3d(planePos, Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z));
                    this.posMax = new Vec3d(planePos, Math.max(pos1.y, pos2.y), Math.max(pos1.z, pos2.z));
                    break;
                case Direction.Axis.Y:
                    this.posMin = new Vec3d(Math.min(pos1.x, pos2.x), planePos, Math.min(pos1.z, pos2.z));
                    this.posMax = new Vec3d(Math.max(pos1.x, pos2.x), planePos, Math.max(pos1.z, pos2.z));
                    break;
                case Direction.Axis.Z:
                    this.posMin = new Vec3d(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), planePos);
                    this.posMax = new Vec3d(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y), planePos);
            }
        }
    }


}
