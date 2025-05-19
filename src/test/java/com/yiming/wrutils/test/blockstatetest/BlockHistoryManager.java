package com.yiming.wrutils.test.blockstatetest;

import java.util.*;

public class BlockHistoryManager {
    // 主结构：key 是方块的位置（x,y,z），value 是该位置的历史状态列表
    private Map<String, List<BlockStatus>> blockPositionMap = new HashMap<>();

    // 辅助结构：key 是时间戳，value 是该时刻的所有方块状态
    private Map<Long, List<BlockStatus>> timeBasedMap = new TreeMap<>(); // 按时间排序

    // 添加某个时刻的方块状态
    public void recordBlockState(BlockStatus state) {
        String key = getKey(state.x, state.y, state.z);

        // 添加到位置索引
        blockPositionMap.computeIfAbsent(key, k -> new ArrayList<>()).add(state);

        // 添加到时间索引
        timeBasedMap.computeIfAbsent(state.timestamp, k -> new ArrayList<>()).add(state);
    }

    // 获取某位置的所有历史状态
    public List<BlockStatus> getBlockHistory(int x, int y, int z) {
        String key = getKey(x, y, z);
        return blockPositionMap.getOrDefault(key, Collections.emptyList());
    }

    // 获取某时刻的所有方块状态
    public List<BlockStatus> getBlockStatesAt(long timestamp) {
        return timeBasedMap.getOrDefault(timestamp, Collections.emptyList());
    }
    /*
    根据给定的tick和方块位置获取BlockStatus 此方法用于在特定时间和位置检索BlockStatus，通过timeBasedMap映射找到对应的BlockStatus列表， 然后遍历列表寻找与给定方块位置匹配的BlockStatus
     */
    public BlockStatus getBlockStatusAtTickAndPosition(long tick, int x, int y, int z) {
        List<BlockStatus> blockStatuses = timeBasedMap.getOrDefault(tick, Collections.emptyList());
        for (BlockStatus blockStatus : blockStatuses) {
            if (blockStatus.x == x && blockStatus.y == y && blockStatus.z == z) {
                return blockStatus;
            }
        }
        return null;
    }




    // 构建唯一 key 的辅助方法（也可以用 Position 对象代替）
    private String getKey(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    public boolean removeBlockStatus(BlockStatus target) {
        String key = getKey(target.x, target.y, target.z);
        List<BlockStatus> list = blockPositionMap.get(key);
        if (list != null && list.remove(target)) {
            // 同步从 timeBasedMap 中删除
            List<BlockStatus> timeList = timeBasedMap.get(target.timestamp);
            if (timeList != null) {
                timeList.remove(target);
                if (timeList.isEmpty()) {
                    timeBasedMap.remove(target.timestamp); // 清理空列表
                }
            }
            return true;
        }
        return false;
    }

}
