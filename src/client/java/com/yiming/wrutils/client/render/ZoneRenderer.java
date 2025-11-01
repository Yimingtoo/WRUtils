package com.yiming.wrutils.client.render;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class ZoneRenderer {

    public static void drawFilledBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, Vec3i pos2, DrawStyle style) {
        Vec3d camPos = camera.getPos();
        double x_len = pos2.getX() - pos1.getX();
        double y_len = pos2.getY() - pos1.getY();
        double z_len = pos2.getZ() - pos1.getZ();

        double x_min = pos1.getX();
        double y_min = pos1.getY();
        double z_min = pos1.getZ();

        if (x_len < 0 || y_len < 0 || z_len < 0) {
            x_len = Math.abs(x_len);
            y_len = Math.abs(y_len);
            z_len = Math.abs(z_len);
            x_min = Math.min(pos1.getX(), pos2.getX());
            y_min = Math.min(pos1.getY(), pos2.getY());
            z_min = Math.min(pos1.getZ(), pos2.getZ());
        }

        matrices.push();
        matrices.translate(x_min - camPos.x, y_min - camPos.y, z_min - camPos.z); // 将矩阵移动到目标位置

        VertexConsumer fillConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugFilledBox());
        VertexRendering.drawFilledBox(
                matrices,
                fillConsumer,
                0, 0, 0,
                (float) x_len + 1, (float) y_len + 1, (float) z_len + 1,
                style.red, style.green, style.blue, style.alpha
        );
        matrices.pop();
    }

    public static void drawBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, Vec3i pos2, DrawStyle style) {
        Vec3d camPos = camera.getPos();
        Vec3i uint = new Vec3i(
                pos2.getX() >= pos1.getX() ? 1 : -1,
                pos2.getY() >= pos1.getY() ? 1 : -1,
                pos2.getZ() >= pos1.getZ() ? 1 : -1
        );
        int dx = (pos2.getX() - pos1.getX()) + uint.getX();
        int dy = (pos2.getY() - pos1.getY()) + uint.getY();
        int dz = (pos2.getZ() - pos1.getZ()) + uint.getZ();

        // 获取大Box的两个角点
        Vec3i p1 = new Vec3i(0, 0, 0);
        Vec3i p7 = p1.add(new Vec3i(dx, dy, dz));
        List<Side> sides = new ArrayList<>(Side.getSides(p1, p7, style));

        matrices.push();
        matrices.translate(pos1.getX() + 0.5 - camPos.x, pos1.getY() + 0.5 - camPos.y, pos1.getZ() + 0.5 - camPos.z); // 将矩阵移动到目标位置
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
        for (Side side : sides) {
            ZoneRenderer.drawLine(
                    entry,
                    lineConsumer,
                    side.pos1.getX() - uint.getX() * 0.5,
                    side.pos1.getY() - uint.getY() * 0.5,
                    side.pos1.getZ() - uint.getZ() * 0.5,
                    side.pos2.getX() - uint.getX() * 0.5,
                    side.pos2.getY() - uint.getY() * 0.5,
                    side.pos2.getZ() - uint.getZ() * 0.5,
                    side.style.color);
        }
        matrices.pop();
    }

    public static void drawSelectedBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, DrawStyle style1, Vec3i pos2, DrawStyle style2, DrawStyle style) {
        Vec3d camPos = camera.getPos();
        Vec3i uint = new Vec3i(
                pos2.getX() >= pos1.getX() ? 1 : -1,
                pos2.getY() >= pos1.getY() ? 1 : -1,
                pos2.getZ() >= pos1.getZ() ? 1 : -1
        );
        int dx = (pos2.getX() - pos1.getX()) + uint.getX();
        int dy = (pos2.getY() - pos1.getY()) + uint.getY();
        int dz = (pos2.getZ() - pos1.getZ()) + uint.getZ();

        // 获取大Box的两个角点
        Vec3i p1 = new Vec3i(0, 0, 0);
        Vec3i p7 = p1.add(new Vec3i(dx, dy, dz));

        // 获取玩家选择的两个方块的剩余的交点
        Vec3i p1_1 = p1.add(new Vec3i(uint.getX(), uint.getY(), uint.getZ()));
        Vec3i p7_1 = p7.add(new Vec3i(-uint.getX(), -uint.getY(), -uint.getZ()));

//        ArrayList<Side> diagonalBlockSides = new ArrayList<>(Side.getSides(p1, p1_1, style1));
//        diagonalBlockSides.addAll(Side.getSides(p7_1, p7, style2));
//        ArrayList<Side> deduplicateSides = new ArrayList<Side>(Side.deduplicateSides(diagonalBlockSides, DrawStyle.getMixedStyle(style1, style2)));
//
//        ArrayList<Side> sides = new ArrayList<>(Side.getSides(p1, p7, style));
//        ArrayList<Side> cutSides = new ArrayList<>();
//
//        for (Side side1 : deduplicateSides) {
//            for (Side side : sides) {
//                ArrayList<Side> tempSides = side.cutSide(side1);
//                if (!tempSides.isEmpty()) {
//                    cutSides.addAll(tempSides);
//                }
//            }
//            sides.clear();
//            sides.addAll(cutSides);
//            cutSides.clear();
//        }

        // 获得pos1和pos2处的两个方块的所有边
        List<Side> diagonalBlockSides = Stream.concat(
                Side.getSides(p1, p1_1, style1).stream(),
                Side.getSides(p7_1, p7, style2).stream()
        ).toList();

        // 去除重复的边
        List<Side> deduplicateSides = Side.deduplicateSides(diagonalBlockSides, DrawStyle.getMixedStyle(style1, style2));

        // 获得大Box的所有边
        List<Side> sides = new ArrayList<>(Side.getSides(p1, p7, style));
        // 剪除sides中与deduplicateSides中重叠的边
        for (Side side1 : deduplicateSides) {
            List<Side> nextSides = new ArrayList<>();
            for (Side side : sides) {
                List<Side> cut = side.cutSide(side1);
                if (!cut.isEmpty()) {
                    nextSides.addAll(cut);
                }
            }
            sides = nextSides;
        }

        // 渲染
        matrices.push();
        matrices.translate(pos1.getX() + 0.5 - camPos.x, pos1.getY() + 0.5 - camPos.y, pos1.getZ() + 0.5 - camPos.z); // 将矩阵移动到目标位置
        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());

        sides.addAll(deduplicateSides);
        for (Side side : sides) {
            ZoneRenderer.drawLine(
                    entry,
                    lineConsumer,
                    side.pos1.getX() - uint.getX() * 0.5,
                    side.pos1.getY() - uint.getY() * 0.5,
                    side.pos1.getZ() - uint.getZ() * 0.5,
                    side.pos2.getX() - uint.getX() * 0.5,
                    side.pos2.getY() - uint.getY() * 0.5,
                    side.pos2.getZ() - uint.getZ() * 0.5,
                    side.style.color);
        }
        matrices.pop();

    }


    public static void drawLine(
            MatrixStack.Entry entry,
            VertexConsumer vertexConsumers,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int color
    ) {
        Vector3f vector3f = new Vector3f((float) (maxX - minX), (float) (maxY - minY), (float) (maxZ - minZ)).normalize();
        vertexConsumers.vertex(entry, (float) minX, (float) minY, (float) minZ).color(color).normal(entry, vector3f);
        vertexConsumers.vertex(entry, (float) maxX, (float) maxY, (float) maxZ).color(color).normal(entry, vector3f);
    }


}
