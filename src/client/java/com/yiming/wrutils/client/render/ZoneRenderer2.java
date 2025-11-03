package com.yiming.wrutils.client.render;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ZoneRenderer2 {

    public static void drawSelectedBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, DrawStyle style1, Vec3i pos2, DrawStyle style2, DrawStyle style) {
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

        // 获得pos1和pos2处的两个方块的所有边
        List<CubeSide> diagonalBlockCubeSides = Stream.concat(
                CubeSide.getSides(p1, p1_1, style1).stream(),
                CubeSide.getSides(p7_1, p7, style2).stream()
        ).toList();

        // 去除重复的边
        List<CubeSide> deduplicateCubeSides = CubeSide.deduplicateSides(diagonalBlockCubeSides, DrawStyle.getMixedStyle(style1, style2));

        // 获得大Box的所有边
        List<CubeSide> cubeSides = new ArrayList<>(CubeSide.getSides(p1, p7, style));
        // 剪除sides中与deduplicateSides中重叠的边
        cubeSides = CubeSide.cutSides(cubeSides, deduplicateCubeSides);

        cubeSides.addAll(deduplicateCubeSides);

        // 渲染
        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(pos1.getX() + 0.5 - camPos.x, pos1.getY() + 0.5 - camPos.y, pos1.getZ() + 0.5 - camPos.z); // 将矩阵移动到目标位置
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugLineStrip(1.0));

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        for (CubeSide cubeSide : cubeSides) {
            ZoneRenderer2.drawLine(
                    matrix4f,
                    lineConsumer,
                    cubeSide.pos1.getX() - uint.getX() * 0.5,
                    cubeSide.pos1.getY() - uint.getY() * 0.5,
                    cubeSide.pos1.getZ() - uint.getZ() * 0.5,
                    cubeSide.pos2.getX() - uint.getX() * 0.5,
                    cubeSide.pos2.getY() - uint.getY() * 0.5,
                    cubeSide.pos2.getZ() - uint.getZ() * 0.5,
                    cubeSide.style.color);
        }
        matrices.pop();

    }

    public static void drawBoxFaces(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, Vec3i pos2, DrawStyle style) {
        CubeFaces cubeFaces = new CubeFaces(pos1, pos2, style);

        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(cubeFaces.getPosMin().getX() - camPos.x, cubeFaces.getPosMin().getY() - camPos.y, cubeFaces.getPosMin().getZ() - camPos.z); // 将矩阵移动到目标位置
//        matrices.translate(0 - camPos.x, 0 - camPos.y, 0 - camPos.z); // 将矩阵移动到目标位置
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugQuads());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        for (Map.Entry<Direction, CubeFaces.CubeFace> entry : cubeFaces.faces.entrySet()) {
            CubeFaces.CubeFace cubeFace = entry.getValue();
            List<Vec3i> points = cubeFace.getPoints(entry.getKey());
            for (Vec3i point : points) {
                vertexConsumer.vertex(matrix4f, point.getX(), point.getY(), point.getZ()).color(cubeFace.style.color);
            }
        }
        matrices.pop();
    }

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

    public static void drawLine(
            Matrix4f matrix4f,
            VertexConsumer vertexConsumer,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int color
    ) {
        vertexConsumer.vertex(matrix4f, (float) minX, (float) minY, (float) minZ).color(color & 0x00FFFFFF);
        vertexConsumer.vertex(matrix4f, (float) minX, (float) minY, (float) minZ).color(color);
        vertexConsumer.vertex(matrix4f, (float) maxX, (float) maxY, (float) maxZ).color(color);
        vertexConsumer.vertex(matrix4f, (float) maxX, (float) maxY, (float) maxZ).color(color & 0x00FFFFFF);
    }


    public static void drawTest(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, Vec3i pos2, DrawStyle style) {
        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(0 - camPos.x, 0 - camPos.y, 0 - camPos.z); // 将矩阵移动到目标位置

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugLineStrip(3.0));
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//        vertexConsumer.vertex(matrix4f, 0, 1,0).color(1.0F, 0.0F, 0.0F, 0.0F);
        vertexConsumer.vertex(matrix4f, 0, 1, 0).color(1.0F, 0.0F, 0.0F, 0.5F);
        vertexConsumer.vertex(matrix4f, 0, 3, 0).color(1.0F, 0.0F, 0.0F, 0.5F);
//        vertexConsumer.vertex(matrix4f, 0, 3,0).color(1.0F, 0.0F, 0.0F, 0.0F);
        vertexConsumer.vertex(matrix4f, 0, 3, 0).color(0.0F, 0.0F, 1.0F, 0.5F);
        vertexConsumer.vertex(matrix4f, 0, 3, 4).color(0.0F, 0.0F, 1.0F, 0.5F);
        matrices.pop();

    }


    public static void drawTest1(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, Vec3i pos2, DrawStyle style) {
        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(0 - camPos.x, 0 - camPos.y, 0 - camPos.z); // 将矩阵移动到目标位置

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugQuads());

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);

        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);

        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);

        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);

        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F);

//        vertexConsumer.vertex(matrix4f, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
//        vertexConsumer.vertex(matrix4f, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
//        vertexConsumer.vertex(matrix4f, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
//        vertexConsumer.vertex(matrix4f, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F);
        matrices.pop();

    }
}
