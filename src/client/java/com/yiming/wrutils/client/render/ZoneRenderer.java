package com.yiming.wrutils.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ZoneRenderer {

    public static void drawSelectedBox(MatrixStack matrices, Camera camera, VertexConsumerProvider vertexConsumerProvider, Vec3i pos1, WrutilsColor style1, Vec3i pos2, WrutilsColor style2, WrutilsColor style) {
        Vec3i unit = new Vec3i(
                pos2.getX() >= pos1.getX() ? 1 : -1,
                pos2.getY() >= pos1.getY() ? 1 : -1,
                pos2.getZ() >= pos1.getZ() ? 1 : -1
        );
        int dx = (pos2.getX() - pos1.getX()) + unit.getX();
        int dy = (pos2.getY() - pos1.getY()) + unit.getY();
        int dz = (pos2.getZ() - pos1.getZ()) + unit.getZ();

        // 获取大Box的两个角点
        Vec3i p1 = new Vec3i(0, 0, 0);
        Vec3i p7 = p1.add(new Vec3i(dx, dy, dz));

        // 获取玩家选择的两个方块的剩余的交点
        Vec3i p1_1 = p1.add(new Vec3i(unit.getX(), unit.getY(), unit.getZ()));
        Vec3i p7_1 = p7.add(new Vec3i(-unit.getX(), -unit.getY(), -unit.getZ()));

        // 获得pos1和pos2处的两个方块的所有边
        List<CubeLine> diagonalBlockCubeLines = Stream.concat(
                CubeLine.getLines(p1, p1_1, style1).stream(),
                CubeLine.getLines(p7_1, p7, style2).stream()
        ).toList();

        // 去除重复的边
        List<CubeLine> deduplicateCubeLines = CubeLine.deduplicateLines(diagonalBlockCubeLines, WrutilsColor.getMixedStyle(style1, style2));

        // 获得大Box的所有边
        List<CubeLine> cubeLines = new ArrayList<>(CubeLine.getLines(p1, p7, style));
        // 剪除lines中与deduplicateLines中重叠的边
        cubeLines = CubeLine.cutLines(cubeLines, deduplicateCubeLines);

        cubeLines.addAll(deduplicateCubeLines);

        // 渲染
        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(pos1.getX() + 0.5 - camPos.x, pos1.getY() + 0.5 - camPos.y, pos1.getZ() + 0.5 - camPos.z); // 将矩阵移动到目标位置
        VertexConsumer lineConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getDebugLineStrip(1.0));

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        for (CubeLine cubeLine : cubeLines) {
            ZoneRenderer.drawLine(
                    matrix4f,
                    lineConsumer,
                    cubeLine.pos1.getX() - unit.getX() * 0.5,
                    cubeLine.pos1.getY() - unit.getY() * 0.5,
                    cubeLine.pos1.getZ() - unit.getZ() * 0.5,
                    cubeLine.pos2.getX() - unit.getX() * 0.5,
                    cubeLine.pos2.getY() - unit.getY() * 0.5,
                    cubeLine.pos2.getZ() - unit.getZ() * 0.5,
                    cubeLine.style.color);
        }
        matrices.pop();

    }

    public static void drawBoxFaces(MatrixStack matrices, Camera camera, Vec3i pos, WrutilsColor style) {
        drawBoxFaces(matrices, camera, pos, pos, style);
    }

    public static void drawBoxFaces(MatrixStack matrices, Camera camera, Vec3i pos1, Vec3i pos2, WrutilsColor style) {
        CubeFaces cubeFaces = new CubeFaces(pos1, pos2, style);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(false);

        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.2f, -0.2f);

        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        //RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        BuiltBuffer meshData;

        Vec3d camPos = camera.getPos();
        matrices.push();
        matrices.translate(cubeFaces.getPosMin().getX() - camPos.x, cubeFaces.getPosMin().getY() - camPos.y, cubeFaces.getPosMin().getZ() - camPos.z); // 将矩阵移动到目标位置
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        for (Map.Entry<Direction, CubeFaces.CubeFace> entry : cubeFaces.faces.entrySet()) {
            CubeFaces.CubeFace cubeFace = entry.getValue();
            List<Vec3d> points = cubeFace.getExpandPoints(entry.getKey());
            for (Vec3d point : points) {
                buffer.vertex(matrix4f, (float) point.getX(), (float) point.getY(), (float) point.getZ()).color(cubeFace.style.color);
            }
        }

        try {
            meshData = buffer.end();
            BufferRenderer.drawWithGlobalProgram(meshData);
            meshData.close();
        } catch (Exception ignored) {
        }
        matrices.pop();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        RenderSystem.polygonOffset(0f, 0f);
        RenderSystem.disablePolygonOffset();

        RenderSystem.depthMask(true);
        RenderSystem.disableDepthTest();
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

}
