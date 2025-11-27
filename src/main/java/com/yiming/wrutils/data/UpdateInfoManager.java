package com.yiming.wrutils.data;

import net.minecraft.util.math.BlockPos;

import java.util.*;

@Deprecated
public class UpdateInfoManager {
    // 主数据：复合键 -> UpdateInfo
    Map<GameTickBlockPosKey, Map<Integer, UpdateInfo>> dataMap = new HashMap<>();

    // 索引1：gameTick -> 所有对应的 UpdateInfo
    Map<Long, List<UpdateInfo>> gameTickIndex = new HashMap<>();

    // 索引2：blockPos -> 所有对应的 UpdateInfo
    Map<Long, List<UpdateInfo>> blockPosIndex = new HashMap<>();

    public void addUpdateInfo(UpdateInfo updateInfo) {
        long gameTick = updateInfo.getGameTick();
        BlockPos blockPos = updateInfo.getBlockPos();
        GameTickBlockPosKey key = new GameTickBlockPosKey(gameTick, blockPos);

        // 更新主数据
        dataMap.computeIfAbsent(key, k -> new HashMap<>()).put(updateInfo.getOrder(), updateInfo);

        // 更新 gameTick 索引
        gameTickIndex.computeIfAbsent(gameTick, k -> new ArrayList<>()).add(updateInfo);

        // 更新 blockPos 索引
        blockPosIndex.computeIfAbsent(blockPos.asLong(), k -> new ArrayList<>()).add(updateInfo);
    }

    public void removeUpdateInfo(UpdateInfo updateInfo) {
        GameTickBlockPosKey key = new GameTickBlockPosKey(updateInfo.getGameTick(), updateInfo.getBlockPos());

        // 从主数据中移除
        Map<Integer, UpdateInfo> orderMap = dataMap.get(key);
        if (orderMap != null) {
            orderMap.remove(updateInfo.getOrder());
            if (orderMap.isEmpty()) {
                dataMap.remove(key);
            }
        }

        // 从 gameTick 索引中移除
        List<UpdateInfo> byGameTick = gameTickIndex.get(updateInfo.getGameTick());
        if (byGameTick != null) {
            byGameTick.remove(updateInfo);
        }

        // 从 blockPos 索引中移除
        List<UpdateInfo> byBlockPos = blockPosIndex.get(updateInfo.getBlockPos().asLong());
        if (byBlockPos != null) {
            byBlockPos.remove(updateInfo);
        }
    }


    public List<UpdateInfo> getUpdateInfosByGameTick(long targetGameTick) {
        return gameTickIndex.getOrDefault(targetGameTick, Collections.emptyList());
    }

    public List<UpdateInfo> getUpdateInfosByBlockPos(BlockPos targetBlockPos) {
        return blockPosIndex.getOrDefault(targetBlockPos.asLong(), Collections.emptyList());
    }

    public List<UpdateInfo> getUpdateInfosByBlockPos(long targetBlockPos) {
        return blockPosIndex.getOrDefault(targetBlockPos, Collections.emptyList());
    }


}
