package com.yiming.wrutils.client.render.deprecated;

import com.yiming.wrutils.client.render.DrawStyle;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.joml.Matrix4f;
import org.joml.Vector3f;
@Deprecated

public class ZoneRenderer1 {
    public static void drawBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3d posMin, Vec3d posMax, DrawStyle style) {
        // 获取摄像机位置
        Vec3d camPos = camera.getPos();
        double x_len = posMax.getX() - posMin.getX();
        double y_len = posMax.getY() - posMin.getY();
        double z_len = posMax.getZ() - posMin.getZ();

        double x_min = posMin.getX();
        double y_min = posMin.getY();
        double z_min = posMin.getZ();

        if (x_len < 0 || y_len < 0 || z_len < 0) {
            x_len = Math.abs(x_len);
            y_len = Math.abs(y_len);
            z_len = Math.abs(z_len);
            x_min = Math.min(posMin.getX(), posMax.getX());
            y_min = Math.min(posMin.getY(), posMax.getY());
            z_min = Math.min(posMin.getZ(), posMax.getZ());
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

    public static void drawLine1(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3d posMin, Vec3d posMax, DrawStyle style) {
        Vec3d camPos = camera.getPos();

        float x_min = (float) posMin.getX();
        float y_min = (float) posMin.getY();
        float z_min = (float) posMin.getZ();
        float x_max = (float) posMax.getX();
        float y_max = (float) posMax.getY();
        float z_max = (float) posMax.getZ();

        matrices.push();
        matrices.translate(x_min - camPos.x, y_min - camPos.y, z_min - camPos.z); // 将矩阵移动到目标位置
        // 渲染轮廓线
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
        ZoneRenderer1.drawLine(matrices, lineConsumer, x_min, y_min, z_min, x_max, y_max, z_max, style.color);
        matrices.pop();
    }

    public static void drawOutline(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3d posMin, Vec3d posMax, DrawStyle style) {

        Vec3d camPos = camera.getPos();
        double x_len = posMax.getX() - posMin.getX();
        double y_len = posMax.getY() - posMin.getY();
        double z_len = posMax.getZ() - posMin.getZ();

        double x_min = posMin.getX();
        double y_min = posMin.getY();
        double z_min = posMin.getZ();

        if (x_len < 0 || y_len < 0 || z_len < 0) {
            x_len = Math.abs(x_len);
            y_len = Math.abs(y_len);
            z_len = Math.abs(z_len);
            x_min = Math.min(posMin.getX(), posMax.getX());
            y_min = Math.min(posMin.getY(), posMax.getY());
            z_min = Math.min(posMin.getZ(), posMax.getZ());
        }
        VoxelShape shape = VoxelShapes.cuboid(0, 0, 0, x_len + 1, y_len + 1, z_len + 1);


        matrices.push();
        matrices.translate(x_min - camPos.x, y_min - camPos.y, z_min - camPos.z); // 将矩阵移动到目标位置

        // 渲染轮廓线
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
        VertexRendering.drawOutline(matrices, lineConsumer, shape, 0, 0, 0, style.color);
        matrices.pop();
    }

    public static void drawFilledBox(
            MatrixStack matrices,
            VertexConsumer vertexConsumers,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        // DOWN:
        vertexConsumers.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha);
        // UP:
        vertexConsumers.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha);
        // NORTH:
        vertexConsumers.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha);
        // SOUTH:
        vertexConsumers.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha);
        // WEST:
        vertexConsumers.vertex(matrix4f, minX, minY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, minY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, maxY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, minX, maxY, minZ).color(red, green, blue, alpha);
        // EAST:
        vertexConsumers.vertex(matrix4f, maxX, minY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, minZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, maxY, maxZ).color(red, green, blue, alpha);
        vertexConsumers.vertex(matrix4f, maxX, minY, maxZ).color(red, green, blue, alpha);
    }

    public static void drawLine(
            MatrixStack matrices,
            VertexConsumer vertexConsumers,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            int color
    ) {
        MatrixStack.Entry entry = matrices.peek();
        Vector3f vector3f = new Vector3f((float) (maxX - minX), (float) (maxY - minY), (float) (maxZ - minZ)).normalize();
        vertexConsumers.vertex(entry, 0, 0, 0).color(color).normal(entry, vector3f);
        vertexConsumers.vertex(entry, (maxX - minX),  (maxY - minY), (maxZ - minZ)).color(color).normal(entry, vector3f);
    }

}
