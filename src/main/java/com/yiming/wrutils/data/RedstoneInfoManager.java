package com.yiming.wrutils.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.TickPriority;

import java.util.*;

public class RedstoneInfoManager {

    // 结构1：key 是方块的位置（x,y,z），value 是该位置的历史状态列表
    private Map<Long, List<RedstoneInfoUnit>> blockPositionMap = new HashMap<>();

    // 结构2：key 是游戏刻，value 是该时刻的所有方块状态 // 按时间排序
    private Map<Long, List<RedstoneInfoUnit>> tickBasedMap = new TreeMap<>();

    /**
     * 向数据结构中添加RedstoneInfoUnit<br>
     * 该方法将RedstoneInfoUnit同时添加到基于方块位置的映射和基于游戏刻的映射中<br>
     * 这样做是为了支持根据方块位置或游戏时间快速查询RedstoneInfoUnit<br>
     *
     * @param unit RedstoneInfoUnit，包含红石相关的数据
     */
    public void addRedstoneInfoUnit(RedstoneInfoUnit unit) {
        blockPositionMap.computeIfAbsent(unit.blockPos.asLong(), k -> new ArrayList<>()).add(unit);
        tickBasedMap.computeIfAbsent(unit.gameTick, k -> new ArrayList<>()).add(unit);
    }

    /**
     * 从数据结构中删除指定的RedstoneInfoUnit<br>
     * 此方法旨在移除给定的RedstoneInfoUnit，它首先根据单元的位置信息从blockPositionMap中查找并删除，<br>
     * 然后从tickBasedMap中移除相同单元，如果移除后列表为空，则清理该列表以节省空间<br>
     *
     * @param target 待删除的RedstoneInfoUnit
     * @return 如果成功删除目标RedstoneInfoUnit，则返回true；否则返回false
     */
    public boolean removeRedstoneInfoUnit(RedstoneInfoUnit target) {
        List<RedstoneInfoUnit> list = blockPositionMap.get(target.blockPos.asLong());
        if (list != null && list.remove(target)) {
            // 同步从 tickBasedMap 中删除
            List<RedstoneInfoUnit> timeList = tickBasedMap.get(target.gameTick);
            if (timeList != null) {
                timeList.remove(target);
                if (timeList.isEmpty()) {
                    tickBasedMap.remove(target.gameTick); // 清理空列表
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 根据给定的tick和方块位置获取RedstoneInfoUnit<br>
     * 此方法用于在特定时间和位置检索RedstoneInfoUnit，通过tickBasedMap映射找到对应的RedstoneInfoUnit列表，<br>
     * 然后遍历列表寻找与给定方块位置匹配的RedstoneInfoUnit<br>
     *
     * @param tick     特定的时间刻，用于检索RedstoneInfoUnit
     * @param blockPos 方块的位置，以长整型表示，用于精确匹配RedstoneInfoUnit
     * @return 如果找到匹配的RedstoneInfoUnit，则返回该单元；否则返回null
     */
    public RedstoneInfoUnit getRedstoneInfoAtTickAndPosition(long tick, long blockPos) {
        List<RedstoneInfoUnit> units = tickBasedMap.getOrDefault(tick, Collections.emptyList());
        for (RedstoneInfoUnit unit : units) {
            if (unit.blockPos.asLong() == blockPos) {
                return unit; // 返回匹配的 RedstoneInfoUnit
            }
        }
        return null; // 没有找到匹配的信息
    }

    /**
     * 根据给定的坐标获取方块的历史记录<br>
     * 该方法使用长整型键来查询blockPositionMap中的RedstoneInfoUnit列表<br>
     * 如果给定的键不存在于map中，则返回一个空列表<br>
     *
     * @param blockPos 方块位置的唯一键
     * @return 对应键的Redstone信息单元列表，如果不存在则返回空列表
     */
    public List<RedstoneInfoUnit> getBlockHistory(long blockPos) {
        return blockPositionMap.getOrDefault(blockPos, Collections.emptyList());
    }

    /**
     * 获取某时刻的所有方块状态，根据给定的时间戳获取RedstoneInfoUnit列表
     *
     * @param tick 时间戳，用于查询特定时间的RedstoneInfoUnit列表
     * @return 对应时间戳的RedstoneInfoUnit列表如果指定时间戳没有对应的列表，则返回一个空列表
     */
    public List<RedstoneInfoUnit> getBlockStatesAt(long tick) {
        return tickBasedMap.getOrDefault(tick, Collections.emptyList());
    }


    public void addUpdateInfoUnit(UpdateInfo updateInfo, BlockPos blockPos) {
        RedstoneInfoUnit redstoneInfoUnit = getRedstoneInfoAtTickAndPosition(updateInfo.getGameTick(), blockPos.asLong());
        if (redstoneInfoUnit != null) {
            redstoneInfoUnit.addUpdateInfoUnit(updateInfo);
        } else {
            RedstoneInfoUnit newRedstoneInfoUnit = new RedstoneInfoUnit(blockPos, updateInfo.getGameTick());
            newRedstoneInfoUnit.addUpdateInfoUnit(updateInfo);
            this.addRedstoneInfoUnit(newRedstoneInfoUnit);
        }
    }


    /**
     * 遍历 tickBasedMap 中的每个 RedstoneInfoUnit，并输出其包含的每个 UpdateInfo 的 tickDescription
     */
    public void printAllUpdateInfoTickDescriptions() {
        System.out.println("printAllUpdateInfoTickDescriptions");
        for (Map.Entry<Long, List<RedstoneInfoUnit>> entry : tickBasedMap.entrySet()) {
            long tick = entry.getKey();
            List<RedstoneInfoUnit> units = entry.getValue();

            for (RedstoneInfoUnit unit : units) {
                // 获取 UpdateInfo 的有序 map
                Map<Integer, UpdateInfo> updateInfoMap = unit.getUpdateInfoMap();

                for (Map.Entry<Integer, UpdateInfo> updateEntry : updateInfoMap.entrySet()) {
                    UpdateInfo updateInfo = updateEntry.getValue();
                    String description = updateInfo.getTickScheduleInfo().getTickDescription();
                    TickPriority tickPriority = updateInfo.getTickScheduleInfo().getTickPriority();
                    Boolean isAddScheduleSuccess = updateInfo.getTickScheduleInfo().isAddScheduleSuccess();
                    int delay = updateInfo.getTickScheduleInfo().getDelay();

                    // 输出信息
                    System.out.printf("Game Tick: %d,\t Block: {%s},\t Order: %d,\t isAddScheduleSuccess:  %b,\t Tick Priority: %s,\t Delay: %d,\t Tick Description: %s%n",
                            tick,
                            unit.blockPos.toShortString(),
                            updateInfo.getOrder(),
                            isAddScheduleSuccess,
                            tickPriority == null ? "NULL" : tickPriority.name(),
                            delay,
                            description == null ? "N/A" : description);

                }
            }
        }
    }


}
