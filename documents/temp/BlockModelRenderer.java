package net.minecraft.client.render.block;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.BitSet;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

/**
 * 渲染方块模型的类
 * 负责处理方块模型的平滑光照和环境光遮蔽计算
 */
@Environment(EnvType.CLIENT)
public class BlockModelRenderer {
    private static final int field_32782 = 0;
    private static final int field_32783 = 1;
    static final Direction[] DIRECTIONS = Direction.values();
    private final BlockColors colors;
    private static final int BRIGHTNESS_CACHE_MAX_SIZE = 100;
    static final ThreadLocal<BlockModelRenderer.BrightnessCache> BRIGHTNESS_CACHE = ThreadLocal.withInitial(BlockModelRenderer.BrightnessCache::new);

    /**
     * 构造函数，初始化方块颜色处理器
     * 
     * @param colors 方块颜色提供器
     */
    public BlockModelRenderer(BlockColors colors) {
        this.colors = colors;
    }

    /**
     * 渲染方块模型的主要入口方法
     * 根据是否启用环境光遮蔽选择渲染模式
     * 
     * @param world 方块渲染视图
     * @param model 烘焙后的模型
     * @param state 方块状态
     * @param pos 方块位置
     * @param matrices 矩阵栈
     * @param vertexConsumer 顶点消费者
     * @param cull 是否剔除不可见面
     * @param random 随机数生成器
     * @param seed 随机种子
     * @param overlay 叠加纹理坐标
     */
    public void render(
            BlockRenderView world,
            BakedModel model,
            BlockState state,
            BlockPos pos,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            boolean cull,
            Random random,
            long seed,
            int overlay
    ) {
        // 判断是否使用平滑光照(AO)
        boolean bl = MinecraftClient.isAmbientOcclusionEnabled() && state.getLuminance() == 0 && model.useAmbientOcclusion();
        // 应用模型偏移
        matrices.translate(state.getModelOffset(pos));

        try {
            if (bl) {
                // 使用平滑光照渲染
                this.renderSmooth(world, model, state, pos, matrices, vertexConsumer, cull, random, seed, overlay);
            } else {
                // 使用平面光照渲染
                this.renderFlat(world, model, state, pos, matrices, vertexConsumer, cull, random, seed, overlay);
            }
        } catch (Throwable var16) {
            // 处理渲染异常
            CrashReport crashReport = CrashReport.create(var16, "Tesselating block model");
            CrashReportSection crashReportSection = crashReport.addElement("Block model being tesselated");
            CrashReportSection.addBlockInfo(crashReportSection, world, pos, state);
            crashReportSection.add("Using AO", bl);
            throw new CrashException(crashReport);
        }
    }

    /**
     * 使用平滑光照渲染方块模型
     * 计算环境光遮蔽效果
     * 
     * @param world 方块渲染视图
     * @param model 烘焙后的模型
     * @param state 方块状态
     * @param pos 方块位置
     * @param matrices 矩阵栈
     * @param vertexConsumer 顶点消费者
     * @param cull 是否剔除不可见面
     * @param random 随机数生成器
     * @param seed 随机种子
     * @param overlay 叠加纹理坐标
     */
    public void renderSmooth(
            BlockRenderView world,
            BakedModel model,
            BlockState state,
            BlockPos pos,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            boolean cull,
            Random random,
            long seed,
            int overlay
    ) {
        // 存储各个方向的边界值
        float[] fs = new float[DIRECTIONS.length * 2];
        // 标志位集合，用于标记计算状态
        BitSet bitSet = new BitSet(3);
        // 环境光遮蔽计算器
        BlockModelRenderer.AmbientOcclusionCalculator ambientOcclusionCalculator = new BlockModelRenderer.AmbientOcclusionCalculator();
        // 可变的位置对象，用于临时计算
        Mutable mutable = pos.mutableCopy();

        // 遍历所有方向渲染对应的面
        for (Direction direction : DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(state, direction, random);
            if (!list.isEmpty()) {
                mutable.set(pos, direction);
                // 如果不禁用剔除或者应该绘制该面
                if (!cull || Block.shouldDrawSide(state, world.getBlockState(mutable), direction)) {
                    this.renderQuadsSmooth(world, state, pos, matrices, vertexConsumer, list, fs, bitSet, ambientOcclusionCalculator, overlay);
                }
            }
        }

        // 渲染无方向的四边形（通常是装饰性元素）
        random.setSeed(seed);
        List<BakedQuad> list2 = model.getQuads(state, null, random);
        if (!list2.isEmpty()) {
            this.renderQuadsSmooth(world, state, pos, matrices, vertexConsumer, list2, fs, bitSet, ambientOcclusionCalculator, overlay);
        }
    }

    /**
     * 使用平面光照渲染方块模型
     * 不计算环境光遮蔽，直接使用世界光照
     * 
     * @param world 方块渲染视图
     * @param model 烘焙后的模型
     * @param state 方块状态
     * @param pos 方块位置
     * @param matrices 矩阵栈
     * @param vertexConsumer 顶点消费者
     * @param cull 是否剔除不可见面
     * @param random 随机数生成器
     * @param seed 随机种子
     * @param overlay 叠加纹理坐标
     */
    public void renderFlat(
            BlockRenderView world,
            BakedModel model,
            BlockState state,
            BlockPos pos,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            boolean cull,
            Random random,
            long seed,
            int overlay
    ) {
        BitSet bitSet = new BitSet(3);
        Mutable mutable = pos.mutableCopy();

        // 遍历所有方向渲染对应的面
        for (Direction direction : DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(state, direction, random);
            if (!list.isEmpty()) {
                mutable.set(pos, direction);
                // 如果不禁用剔除或者应该绘制该面
                if (!cull || Block.shouldDrawSide(state, world.getBlockState(mutable), direction)) {
                    // 获取光照坐标
                    int i = WorldRenderer.getLightmapCoordinates(world, state, mutable);
                    this.renderQuadsFlat(world, state, pos, i, overlay, false, matrices, vertexConsumer, list, bitSet);
                }
            }
        }

        // 渲染无方向的四边形
        random.setSeed(seed);
        List<BakedQuad> list2 = model.getQuads(state, null, random);
        if (!list2.isEmpty()) {
            this.renderQuadsFlat(world, state, pos, -1, overlay, true, matrices, vertexConsumer, list2, bitSet);
        }
    }

    /**
     * 渲染具有平滑光照的四边形
     * 
     * @param world 方块渲染视图
     * @param state 方块状态
     * @param pos 方块位置
     * @param matrices 矩阵栈
     * @param vertexConsumer 顶点消费者
     * @param quads 四边形列表
     * @param box 边界值数组
     * @param flags 标志位集合
     * @param ambientOcclusionCalculator 环境光遮蔽计算器
     * @param overlay 叠加纹理坐标
     */
    private void renderQuadsSmooth(
            BlockRenderView world,
            BlockState state,
            BlockPos pos,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            List<BakedQuad> quads,
            float[] box,
            BitSet flags,
            BlockModelRenderer.AmbientOcclusionCalculator ambientOcclusionCalculator,
            int overlay
    ) {
        for (BakedQuad bakedQuad : quads) {
            // 获取四边形的尺寸信息
            this.getQuadDimensions(world, state, pos, bakedQuad.getVertexData(), bakedQuad.getFace(), box, flags);
            // 应用环境光遮蔽计算
            ambientOcclusionCalculator.apply(world, state, pos, bakedQuad.getFace(), box, flags, bakedQuad.hasShade());
            // 渲染单个四边形
            this.renderQuad(
                    world,
                    state,
                    pos,
                    vertexConsumer,
                    matrices.peek(),
                    bakedQuad,
                    ambientOcclusionCalculator.brightness[0],
                    ambientOcclusionCalculator.brightness[1],
                    ambientOcclusionCalculator.brightness[2],
                    ambientOcclusionCalculator.brightness[3],
                    ambientOcclusionCalculator.light[0],
                    ambientOcclusionCalculator.light[1],
                    ambientOcclusionCalculator.light[2],
                    ambientOcclusionCalculator.light[3],
                    overlay
            );
        }
    }

    /**
     * 渲染单个四边形
     * 
     * @param world 方块渲染视图
     * @param state 方块状态
     * @param pos 方块位置
     * @param vertexConsumer 顶点消费者
     * @param matrixEntry 矩阵项
     * @param quad 四边形
     * @param brightness0 顶点0亮度
     * @param brightness1 顶点1亮度
     * @param brightness2 顶点2亮度
     * @param brightness3 顶点3亮度
     * @param light0 顶点0光照
     * @param light1 顶点1光照
     * @param light2 顶点2光照
     * @param light3 顶点3光照
     * @param overlay 叠加纹理坐标
     */
    private void renderQuad(
            BlockRenderView world,
            BlockState state,
            BlockPos pos,
            VertexConsumer vertexConsumer,
            MatrixStack.Entry matrixEntry,
            BakedQuad quad,
            float brightness0,
            float brightness1,
            float brightness2,
            float brightness3,
            int light0,
            int light1,
            int light2,
            int light3,
            int overlay
    ) {
        float f;
        float g;
        float h;
        // 如果四边形有色调，则获取对应的颜色
        if (quad.hasTint()) {
            int i = this.colors.getColor(state, world, pos, quad.getTintIndex());
            f = (float)(i >> 16 & 0xFF) / 255.0F;
            g = (float)(i >> 8 & 0xFF) / 255.0F;
            h = (float)(i & 0xFF) / 255.0F;
        } else {
            // 默认白色
            f = 1.0F;
            g = 1.0F;
            h = 1.0F;
        }

        // 调用顶点消费者渲染四边形
        vertexConsumer.quad(
                matrixEntry, quad, new float[]{brightness0, brightness1, brightness2, brightness3}, f, g, h, 1.0F, new int[]{light0, light1, light2, light3}, overlay, true
        );
    }

    /**
     * 获取四边形的尺寸信息
     * 
     * @param world 方块渲染视图
     * @param state 方块状态
     * @param pos 方块位置
     * @param vertexData 顶点数据
     * @param face 面方向
     * @param box 边界值数组（可为null）
     * @param flags 标志位集合
     */
    private void getQuadDimensions(BlockRenderView world, BlockState state, BlockPos pos, int[] vertexData, Direction face, @Nullable float[] box, BitSet flags) {
        // 初始化边界值
        float f = 32.0F;
        float g = 32.0F;
        float h = 32.0F;
        float i = -32.0F;
        float j = -32.0F;
        float k = -32.0F;

        // 遍历四个顶点，计算边界值
        for (int l = 0; l < 4; l++) {
            float m = Float.intBitsToFloat(vertexData[l * 8]);
            float n = Float.intBitsToFloat(vertexData[l * 8 + 1]);
            float o = Float.intBitsToFloat(vertexData[l * 8 + 2]);
            f = Math.min(f, m);
            g = Math.min(g, n);
            h = Math.min(h, o);
            i = Math.max(i, m);
            j = Math.max(j, n);
            k = Math.max(k, o);
        }

        // 如果提供了边界值数组，则填充数据
        if (box != null) {
            box[Direction.WEST.getId()] = f;
            box[Direction.EAST.getId()] = i;
            box[Direction.DOWN.getId()] = g;
            box[Direction.UP.getId()] = j;
            box[Direction.NORTH.getId()] = h;
            box[Direction.SOUTH.getId()] = k;
            int l = DIRECTIONS.length;
            box[Direction.WEST.getId() + l] = 1.0F - f;
            box[Direction.EAST.getId() + l] = 1.0F - i;
            box[Direction.DOWN.getId() + l] = 1.0F - g;
            box[Direction.UP.getId() + l] = 1.0F - j;
            box[Direction.NORTH.getId() + l] = 1.0F - h;
            box[Direction.SOUTH.getId() + l] = 1.0F - k;
        }

        // 设置标志位
        float p = 1.0E-4F;
        float m = 0.9999F;
        switch (face) {
            case DOWN:
                // 检查是否需要进行环境光遮蔽计算
                flags.set(1, f >= 1.0E-4F || h >= 1.0E-4F || i <= 0.9999F || k <= 0.9999F);
                // 检查是否是完整立方体的面
                flags.set(0, g == j && (g < 1.0E-4F || state.isFullCube(world, pos)));
                break;
            case UP:
                flags.set(1, f >= 1.0E-4F || h >= 1.0E-4F || i <= 0.9999F || k <= 0.9999F);
                flags.set(0, g == j && (j > 0.9999F || state.isFullCube(world, pos)));
                break;
            case NORTH:
                flags.set(1, f >= 1.0E-4F || g >= 1.0E-4F || i <= 0.9999F || j <= 0.9999F);
                flags.set(0, h == k && (h < 1.0E-4F || state.isFullCube(world, pos)));
                break;
            case SOUTH:
                flags.set(1, f >= 1.0E-4F || g >= 1.0E-4F || i <= 0.9999F || j <= 0.9999F);
                flags.set(0, h == k && (k > 0.9999F || state.isFullCube(world, pos)));
                break;
            case WEST:
                flags.set(1, g >= 1.0E-4F || h >= 1.0E-4F || j <= 0.9999F || k <= 0.9999F);
                flags.set(0, f == i && (f < 1.0E-4F || state.isFullCube(world, pos)));
                break;
            case EAST:
                flags.set(1, g >= 1.0E-4F || h >= 1.0E-4F || j <= 0.9999F || k <= 0.9999F);
                flags.set(0, f == i && (i > 0.9999F || state.isFullCube(world, pos)));
        }
    }

    /**
     * 渲染具有平面光照的四边形
     * 
     * @param world 方块渲染视图
     * @param state 方块状态
     * @param pos 方块位置
     * @param light 光照值
     * @param overlay 叠加纹理坐标
     * @param useWorldLight 是否使用世界光照
     * @param matrices 矩阵栈
     * @param vertexConsumer 顶点消费者
     * @param quads 四边形列表
     * @param flags 标志位集合
     */
    private void renderQuadsFlat(
            BlockRenderView world,
            BlockState state,
            BlockPos pos,
            int light,
            int overlay,
            boolean useWorldLight,
            MatrixStack matrices,
            VertexConsumer vertexConsumer,
            List<BakedQuad> quads,
            BitSet flags
    ) {
        for (BakedQuad bakedQuad : quads) {
            // 如果使用世界光照，需要重新计算光照值
            if (useWorldLight) {
                this.getQuadDimensions(world, state, pos, bakedQuad.getVertexData(), bakedQuad.getFace(), null, flags);
                BlockPos blockPos = flags.get(0) ? pos.offset(bakedQuad.getFace()) : pos;
                light = WorldRenderer.getLightmapCoordinates(world, state, blockPos);
            }

            // 获取面的亮度
            float f = world.getBrightness(bakedQuad.getFace(), bakedQuad.hasShade());
            // 渲染四边形
            this.renderQuad(world, state, pos, vertexConsumer, matrices.peek(), bakedQuad, f, f, f, f, light, light, light, light, overlay);
        }
    }

    /**
     * 渲染静态方块模型（不进行环境光遮蔽计算）
     * 
     * @param entry 矩阵项
     * @param vertexConsumer 顶点消费者
     * @param state 方块状态
     * @param bakedModel 烘焙后的模型
     * @param red 红色分量
     * @param green 绿色分量
     * @param blue 蓝色分量
     * @param light 光照值
     * @param overlay 叠加纹理坐标
     */
    public void render(
            MatrixStack.Entry entry,
            VertexConsumer vertexConsumer,
            @Nullable BlockState state,
            BakedModel bakedModel,
            float red,
            float green,
            float blue,
            int light,
            int overlay
    ) {
        Random random = Random.create();
        long l = 42L;

        // 遍历所有方向渲染四边形
        for (Direction direction : DIRECTIONS) {
            random.setSeed(42L);
            renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, direction, random), light, overlay);
        }

        // 渲染无方向的四边形
        random.setSeed(42L);
        renderQuads(entry, vertexConsumer, red, green, blue, bakedModel.getQuads(state, null, random), light, overlay);
    }

    /**
     * 渲染四边形列表
     * 
     * @param entry 矩阵项
     * @param vertexConsumer 顶点消费者
     * @param red 红色分量
     * @param green 绿色分量
     * @param blue 蓝色分量
     * @param quads 四边形列表
     * @param light 光照值
     * @param overlay 叠加纹理坐标
     */
    private static void renderQuads(
            MatrixStack.Entry entry, VertexConsumer vertexConsumer, float red, float green, float blue, List<BakedQuad> quads, int light, int overlay
    ) {
        for (BakedQuad bakedQuad : quads) {
            float f;
            float g;
            float h;
            // 如果四边形有色调，则使用指定颜色，否则使用白色
            if (bakedQuad.hasTint()) {
                f = MathHelper.clamp(red, 0.0F, 1.0F);
                g = MathHelper.clamp(green, 0.0F, 1.0F);
                h = MathHelper.clamp(blue, 0.0F, 1.0F);
            } else {
                f = 1.0F;
                g = 1.0F;
                h = 1.0F;
            }

            // 渲染四边形
            vertexConsumer.quad(entry, bakedQuad, f, g, h, 1.0F, light, overlay);
        }
    }

    /**
     * 启用亮度缓存
     */
    public static void enableBrightnessCache() {
        ((BlockModelRenderer.BrightnessCache)BRIGHTNESS_CACHE.get()).enable();
    }

    /**
     * 禁用亮度缓存
     */
    public static void disableBrightnessCache() {
        ((BlockModelRenderer.BrightnessCache)BRIGHTNESS_CACHE.get()).disable();
    }

    /**
     * 环境光遮蔽计算器
     * 负责计算四边形顶点的环境光遮蔽效果
     */
    @Environment(EnvType.CLIENT)
    static class AmbientOcclusionCalculator {
        final float[] brightness = new float[4];
        final int[] light = new int[4];

        public AmbientOcclusionCalculator() {
        }

        /**
         * 应用环境光遮蔽计算
         * 
         * @param world 方块渲染视图
         * @param state 方块状态
         * @param pos 方块位置
         * @param direction 面方向
         * @param fs 边界值数组
         * @param bitSet 标志位集合
         * @param bl 是否有阴影
         */
        public void apply(BlockRenderView world, BlockState state, BlockPos pos, Direction direction, float[] fs, BitSet bitSet, boolean bl) {
            BlockPos blockPos = bitSet.get(0) ? pos.offset(direction) : pos;
            BlockModelRenderer.NeighborData neighborData = BlockModelRenderer.NeighborData.getData(direction);
            Mutable mutable = new Mutable();
            BlockModelRenderer.BrightnessCache brightnessCache = (BlockModelRenderer.BrightnessCache)BlockModelRenderer.BRIGHTNESS_CACHE.get();
            
            // 获取邻近方块的信息
            mutable.set(blockPos, neighborData.faces[0]);
            BlockState blockState = world.getBlockState(mutable);
            int i = brightnessCache.getInt(blockState, world, mutable);
            float f = brightnessCache.getFloat(blockState, world, mutable);
            
            mutable.set(blockPos, neighborData.faces[1]);
            BlockState blockState2 = world.getBlockState(mutable);
            int j = brightnessCache.getInt(blockState2, world, mutable);
            float g = brightnessCache.getFloat(blockState2, world, mutable);
            
            mutable.set(blockPos, neighborData.faces[2]);
            BlockState blockState3 = world.getBlockState(mutable);
            int k = brightnessCache.getInt(blockState3, world, mutable);
            float h = brightnessCache.getFloat(blockState3, world, mutable);
            
            mutable.set(blockPos, neighborData.faces[3]);
            BlockState blockState4 = world.getBlockState(mutable);
            int l = brightnessCache.getInt(blockState4, world, mutable);
            float m = brightnessCache.getFloat(blockState4, world, mutable);
            
            // 检查邻近方块是否阻挡视线
            BlockState blockState5 = world.getBlockState(mutable.set(blockPos, neighborData.faces[0]).move(direction));
            boolean bl2 = !blockState5.shouldBlockVision(world, mutable) || blockState5.getOpacity() == 0;
            
            BlockState blockState6 = world.getBlockState(mutable.set(blockPos, neighborData.faces[1]).move(direction));
            boolean bl3 = !blockState6.shouldBlockVision(world, mutable) || blockState6.getOpacity() == 0;
            
            BlockState blockState7 = world.getBlockState(mutable.set(blockPos, neighborData.faces[2]).move(direction));
            boolean bl4 = !blockState7.shouldBlockVision(world, mutable) || blockState7.getOpacity() == 0;
            
            BlockState blockState8 = world.getBlockState(mutable.set(blockPos, neighborData.faces[3]).move(direction));
            boolean bl5 = !blockState8.shouldBlockVision(world, mutable) || blockState8.getOpacity() == 0;
            
            // 计算角落的光照值
            float n;
            int o;
            if (!bl4 && !bl2) {
                n = f;
                o = i;
            } else {
                mutable.set(blockPos, neighborData.faces[0]).move(neighborData.faces[2]);
                BlockState blockState9 = world.getBlockState(mutable);
                n = brightnessCache.getFloat(blockState9, world, mutable);
                o = brightnessCache.getInt(blockState9, world, mutable);
            }

            float p;
            int q;
            if (!bl5 && !bl2) {
                p = f;
                q = i;
            } else {
                mutable.set(blockPos, neighborData.faces[0]).move(neighborData.faces[3]);
                BlockState blockState9 = world.getBlockState(mutable);
                p = brightnessCache.getFloat(blockState9, world, mutable);
                q = brightnessCache.getInt(blockState9, world, mutable);
            }

            float r;
            int s;
            if (!bl4 && !bl3) {
                r = f;
                s = i;
            } else {
                mutable.set(blockPos, neighborData.faces[1]).move(neighborData.faces[2]);
                BlockState blockState9 = world.getBlockState(mutable);
                r = brightnessCache.getFloat(blockState9, world, mutable);
                s = brightnessCache.getInt(blockState9, world, mutable);
            }

            float t;
            int u;
            if (!bl5 && !bl3) {
                t = f;
                u = i;
            } else {
                mutable.set(blockPos, neighborData.faces[1]).move(neighborData.faces[3]);
                BlockState blockState9 = world.getBlockState(mutable);
                t = brightnessCache.getFloat(blockState9, world, mutable);
                u = brightnessCache.getInt(blockState9, world, mutable);
            }

            // 获取中心方块的光照值
            int v = brightnessCache.getInt(state, world, pos);
            mutable.set(pos, direction);
            BlockState blockState10 = world.getBlockState(mutable);
            if (bitSet.get(0) || !blockState10.isOpaqueFullCube()) {
                v = brightnessCache.getInt(blockState10, world, mutable);
            }

            // 获取环境光遮蔽等级
            float w = bitSet.get(0)
                    ? brightnessCache.getFloat(world.getBlockState(blockPos), world, blockPos)
                    : brightnessCache.getFloat(world.getBlockState(pos), world, pos);
            
            BlockModelRenderer.Translation translation = BlockModelRenderer.Translation.getTranslations(direction);
            
            // 如果需要进行非立方体权重计算
            if (bitSet.get(1) && neighborData.nonCubicWeight) {
                // 计算四个角的平均光照值
                float x = (m + f + p + w) * 0.25F;
                float y = (h + f + n + w) * 0.25F;
                float z = (h + g + r + w) * 0.25F;
                float aa = (m + g + t + w) * 0.25F;
                
                // 计算权重因子
                float ab = fs[neighborData.field_4192[0].shape] * fs[neighborData.field_4192[1].shape];
                float ac = fs[neighborData.field_4192[2].shape] * fs[neighborData.field_4192[3].shape];
                float ad = fs[neighborData.field_4192[4].shape] * fs[neighborData.field_4192[5].shape];
                float ae = fs[neighborData.field_4192[6].shape] * fs[neighborData.field_4192[7].shape];
                float af = fs[neighborData.field_4185[0].shape] * fs[neighborData.field_4185[1].shape];
                float ag = fs[neighborData.field_4185[2].shape] * fs[neighborData.field_4185[3].shape];
                float ah = fs[neighborData.field_4185[4].shape] * fs[neighborData.field_4185[5].shape];
                float ai = fs[neighborData.field_4185[6].shape] * fs[neighborData.field_4185[7].shape];
                float aj = fs[neighborData.field_4180[0].shape] * fs[neighborData.field_4180[1].shape];
                float ak = fs[neighborData.field_4180[2].shape] * fs[neighborData.field_4180[3].shape];
                float al = fs[neighborData.field_4180[4].shape] * fs[neighborData.field_4180[5].shape];
                float am = fs[neighborData.field_4180[6].shape] * fs[neighborData.field_4180[7].shape];
                float an = fs[neighborData.field_4188[0].shape] * fs[neighborData.field_4188[1].shape];
                float ao = fs[neighborData.field_4188[2].shape] * fs[neighborData.field_4188[3].shape];
                float ap = fs[neighborData.field_4188[4].shape] * fs[neighborData.field_4188[5].shape];
                float aq = fs[neighborData.field_4188[6].shape] * fs[neighborData.field_4188[7].shape];
                
                // 计算每个顶点的亮度
                this.brightness[translation.firstCorner] = Math.clamp(x * ab + y * ac + z * ad + aa * ae, 0.0F, 1.0F);
                this.brightness[translation.secondCorner] = Math.clamp(x * af + y * ag + z * ah + aa * ai, 0.0F, 1.0F);
                this.brightness[translation.thirdCorner] = Math.clamp(x * aj + y * ak + z * al + aa * am, 0.0F, 1.0F);
                this.brightness[translation.fourthCorner] = Math.clamp(x * an + y * ao + z * ap + aa * aq, 0.0F, 1.0F);
                
                // 计算环境光遮蔽亮度
                int ar = this.getAmbientOcclusionBrightness(l, i, q, v);
                int as = this.getAmbientOcclusionBrightness(k, i, o, v);
                int at = this.getAmbientOcclusionBrightness(k, j, s, v);
                int au = this.getAmbientOcclusionBrightness(l, j, u, v);
                
                // 计算每个顶点的光照
                this.light[translation.firstCorner] = this.getBrightness(ar, as, at, au, ab, ac, ad, ae);
                this.light[translation.secondCorner] = this.getBrightness(ar, as, at, au, af, ag, ah, ai);
                this.light[translation.thirdCorner] = this.getBrightness(ar, as, at, au, aj, ak, al, am);
                this.light[translation.fourthCorner] = this.getBrightness(ar, as, at, au, an, ao, ap, aq);
            } else {
                // 简单的平均光照计算
                float x = (m + f + p + w) * 0.25F;
                float y = (h + f + n + w) * 0.25F;
                float z = (h + g + r + w) * 0.25F;
                float aa = (m + g + t + w) * 0.25F;
                
                // 设置环境光遮蔽亮度
                this.light[translation.firstCorner] = this.getAmbientOcclusionBrightness(l, i, q, v);
                this.light[translation.secondCorner] = this.getAmbientOcclusionBrightness(k, i, o, v);
                this.light[translation.thirdCorner] = this.getAmbientOcclusionBrightness(k, j, s, v);
                this.light[translation.fourthCorner] = this.getAmbientOcclusionBrightness(l, j, u, v);
                
                // 设置顶点亮度
                this.brightness[translation.firstCorner] = x;
                this.brightness[translation.secondCorner] = y;
                this.brightness[translation.thirdCorner] = z;
                this.brightness[translation.fourthCorner] = aa;
            }

            // 应用面的亮度
            float x = world.getBrightness(direction, bl);

            for (int av = 0; av < this.brightness.length; av++) {
                this.brightness[av] = this.brightness[av] * x;
            }
        }

        /**
         * 计算环境光遮蔽亮度
         * 
         * @param i 亮度值1
         * @param j 亮度值2
         * @param k 亮度值3
         * @param l 亮度值4
         * @return 计算后的亮度值
         */
        private int getAmbientOcclusionBrightness(int i, int j, int k, int l) {
            // 如果亮度为0，则使用中心亮度
            if (i == 0) {
                i = l;
            }

            if (j == 0) {
                j = l;
            }

            if (k == 0) {
                k = l;
            }

            // 计算平均亮度
            return i + j + k + l >> 2 & 16711935;
        }

        /**
         * 根据权重计算亮度
         * 
         * @param i 亮度值1
         * @param j 亮度值2
         * @param k 亮度值3
         * @param l 亮度值4
         * @param f 权重1
         * @param g 权重2
         * @param h 权重3
         * @param m 权重4
         * @return 计算后的亮度值
         */
        private int getBrightness(int i, int j, int k, int l, float f, float g, float h, float m) {
            // 分别计算红蓝和绿通道的亮度
            int n = (int)((float)(i >> 16 & 0xFF) * f + (float)(j >> 16 & 0xFF) * g + (float)(k >> 16 & 0xFF) * h + (float)(l >> 16 & 0xFF) * m) & 0xFF;
            int o = (int)((float)(i & 0xFF) * f + (float)(j & 0xFF) * g + (float)(k & 0xFF) * h + (float)(l & 0xFF) * m) & 0xFF;
            return n << 16 | o;
        }
    }

    /**
     * 亮度缓存类
     * 用于缓存方块的光照和环境光遮蔽值，提高渲染性能
     */
    @Environment(EnvType.CLIENT)
    static class BrightnessCache {
        private boolean enabled;
        // 整数缓存（用于光照坐标）
        private final Long2IntLinkedOpenHashMap intCache = (Long2IntLinkedOpenHashMap)Util.make(() -> {
            Long2IntLinkedOpenHashMap long2IntLinkedOpenHashMap = new Long2IntLinkedOpenHashMap(100, 0.25F) {
                @Override
                protected void rehash(int newN) {
                }
            };
            long2IntLinkedOpenHashMap.defaultReturnValue(Integer.MAX_VALUE);
            return long2IntLinkedOpenHashMap;
        });
        // 浮点数缓存（用于环境光遮蔽等级）
        private final Long2FloatLinkedOpenHashMap floatCache = (Long2FloatLinkedOpenHashMap)Util.make(() -> {
            Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap = new Long2FloatLinkedOpenHashMap(100, 0.25F) {
                @Override
                protected void rehash(int newN) {
                }
            };
            long2FloatLinkedOpenHashMap.defaultReturnValue(Float.NaN);
            return long2FloatLinkedOpenHashMap;
        });

        private BrightnessCache() {
        }

        /**
         * 启用缓存
         */
        public void enable() {
            this.enabled = true;
        }

        /**
         * 禁用缓存并清空缓存数据
         */
        public void disable() {
            this.enabled = false;
            this.intCache.clear();
            this.floatCache.clear();
        }

        /**
         * 获取光照坐标
         * 
         * @param state 方块状态
         * @param world 方块渲染视图
         * @param pos 方块位置
         * @return 光照坐标
         */
        public int getInt(BlockState state, BlockRenderView world, BlockPos pos) {
            long l = pos.asLong();
            // 如果启用缓存，先尝试从缓存获取
            if (this.enabled) {
                int i = this.intCache.get(l);
                if (i != Integer.MAX_VALUE) {
                    return i;
                }
            }

            // 计算光照坐标
            int i = WorldRenderer.getLightmapCoordinates(world, state, pos);
            // 如果启用缓存，将结果存入缓存
            if (this.enabled) {
                if (this.intCache.size() == 100) {
                    this.intCache.removeFirstInt();
                }

                this.intCache.put(l, i);
            }

            return i;
        }

        /**
         * 获取环境光遮蔽等级
         * 
         * @param state 方块状态
         * @param blockView 方块渲染视图
         * @param pos 方块位置
         * @return 环境光遮蔽等级
         */
        public float getFloat(BlockState state, BlockRenderView blockView, BlockPos pos) {
            long l = pos.asLong();
            // 如果启用缓存，先尝试从缓存获取
            if (this.enabled) {
                float f = this.floatCache.get(l);
                if (!Float.isNaN(f)) {
                    return f;
                }
            }

            // 计算环境光遮蔽等级
            float f = state.getAmbientOcclusionLightLevel(blockView, pos);
            // 如果启用缓存，将结果存入缓存
            if (this.enabled) {
                if (this.floatCache.size() == 100) {
                    this.floatCache.removeFirstFloat();
                }

                this.floatCache.put(l, f);
            }

            return f;
        }
    }

    /**
     * 邻近数据枚举
     * 定义了各个方向的邻近方块信息
     */
    @Environment(EnvType.CLIENT)
    protected static enum NeighborData {
        // 下方向的邻近数据
        DOWN(
                new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH},
                0.5F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                }
        ),
        // 上方向的邻近数据
        UP(
                new Direction[]{Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH},
                1.0F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                }
        ),
        // 北方向的邻近数据
        NORTH(
                new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST},
                0.8F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST
                }
        ),
        // 南方向的邻近数据
        SOUTH(
                new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP},
                0.8F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.WEST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_WEST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.WEST,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.WEST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.EAST
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_EAST,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.EAST,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.EAST
                }
        ),
        // 西方向的邻近数据
        WEST(
                new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH},
                0.6F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                }
        ),
        // 东方向的邻近数据
        EAST(
                new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH},
                0.6F,
                true,
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.DOWN,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.NORTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_NORTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.NORTH
                },
                new BlockModelRenderer.NeighborOrientation[]{
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.SOUTH,
                        BlockModelRenderer.NeighborOrientation.FLIP_UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.FLIP_SOUTH,
                        BlockModelRenderer.NeighborOrientation.UP,
                        BlockModelRenderer.NeighborOrientation.SOUTH
                }
        );

        final Direction[] faces;
        final boolean nonCubicWeight;
        final BlockModelRenderer.NeighborOrientation[] field_4192;
        final BlockModelRenderer.NeighborOrientation[] field_4185;
        final BlockModelRenderer.NeighborOrientation[] field_4180;
        final BlockModelRenderer.NeighborOrientation[] field_4188;
        private static final BlockModelRenderer.NeighborData[] VALUES = (BlockModelRenderer.NeighborData[])Util.make(
                new BlockModelRenderer.NeighborData[6], values -> {
                    values[Direction.DOWN.getId()] = DOWN;
                    values[Direction.UP.getId()] = UP;
                    values[Direction.NORTH.getId()] = NORTH;
                    values[Direction.SOUTH.getId()] = SOUTH;
                    values[Direction.WEST.getId()] = WEST;
                    values[Direction.EAST.getId()] = EAST;
                }
        );

        private NeighborData(
                final Direction[] faces,
                final float f,
                final boolean nonCubicWeight,
                final BlockModelRenderer.NeighborOrientation[] neighborOrientations,
                final BlockModelRenderer.NeighborOrientation[] neighborOrientations2,
                final BlockModelRenderer.NeighborOrientation[] neighborOrientations3,
                final BlockModelRenderer.NeighborOrientation[] neighborOrientations4
        ) {
            this.faces = faces;
            this.nonCubicWeight = nonCubicWeight;
            this.field_4192 = neighborOrientations;
            this.field_4185 = neighborOrientations2;
            this.field_4180 = neighborOrientations3;
            this.field_4188 = neighborOrientations4;
        }

        /**
         * 根据方向获取对应的邻近数据
         * 
         * @param direction 方向
         * @return 对应的邻近数据
         */
        public static BlockModelRenderer.NeighborData getData(Direction direction) {
            return VALUES[direction.getId()];
        }
    }

    /**
     * 邻近方向枚举
     * 定义了邻近方块的方向信息
     */
    @Environment(EnvType.CLIENT)
    protected static enum NeighborOrientation {
        DOWN(Direction.DOWN, false),
        UP(Direction.UP, false),
        NORTH(Direction.NORTH, false),
        SOUTH(Direction.SOUTH, false),
        WEST(Direction.WEST, false),
        EAST(Direction.EAST, false),
        FLIP_DOWN(Direction.DOWN, true),
        FLIP_UP(Direction.UP, true),
        FLIP_NORTH(Direction.NORTH, true),
        FLIP_SOUTH(Direction.SOUTH, true),
        FLIP_WEST(Direction.WEST, true),
        FLIP_EAST(Direction.EAST, true);

        final int shape;

        private NeighborOrientation(final Direction direction, final boolean flip) {
            this.shape = direction.getId() + (flip ? BlockModelRenderer.DIRECTIONS.length : 0);
        }
    }

    /**
     * 翻译枚举
     * 定义了不同方向的顶点顺序映射
     */
    @Environment(EnvType.CLIENT)
    static enum Translation {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        final int firstCorner;
        final int secondCorner;
        final int thirdCorner;
        final int fourthCorner;
        private static final BlockModelRenderer.Translation[] VALUES = (BlockModelRenderer.Translation[])Util.make(new BlockModelRenderer.Translation[6], values -> {
            values[Direction.DOWN.getId()] = DOWN;
            values[Direction.UP.getId()] = UP;
            values[Direction.NORTH.getId()] = NORTH;
            values[Direction.SOUTH.getId()] = SOUTH;
            values[Direction.WEST.getId()] = WEST;
            values[Direction.EAST.getId()] = EAST;
        });

        private Translation(final int firstCorner, final int secondCorner, final int thirdCorner, final int fourthCorner) {
            this.firstCorner = firstCorner;
            this.secondCorner = secondCorner;
            this.thirdCorner = thirdCorner;
            this.fourthCorner = fourthCorner;
        }

        /**
         * 根据方向获取对应的顶点顺序映射
         * 
         * @param direction 方向
         * @return 对应的顶点顺序映射
         */
        public static BlockModelRenderer.Translation getTranslations(Direction direction) {
            return VALUES[direction.getId()];
        }
    }
}
