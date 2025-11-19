package com.yiming.wrutils.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class WrutilsClientUtils {
    public static BlockPos getLookingBlockPosClient(MinecraftClient client, double maxDistance, float tickDelta) {

        if (client.player == null) return null;

        HitResult hit = client.player.raycast(maxDistance, tickDelta, false); // false = 不命中流体
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) hit;
            return bhr.getBlockPos(); // 最近的非空气方块位置
        }
        return null; // 没有命中方块
    }
}
