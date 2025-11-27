package com.yiming.wrutils.data;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Deprecated

public class RedstoneInfoUnit {
    public BlockPos blockPos;
    public long gameTick;     // 游戏刻

    private Map<Integer, UpdateInfo> updateInfoMap = new TreeMap<>();

    public void addUpdateInfoUnit(UpdateInfo updateInfo) {
        updateInfoMap.put(updateInfo.getOrder(),updateInfo);
    }

    public Map<Integer, UpdateInfo> getUpdateInfoMap() {
        return updateInfoMap;
    }


    public RedstoneInfoUnit(BlockPos blockPos, long gameTick) {
        this.blockPos = blockPos;
        this.gameTick = gameTick;
    }


}
