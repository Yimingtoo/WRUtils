package com.yiming.wrutils.test;

import com.yiming.wrutils.test.blockstatetest.BlockHistoryManager;
import com.yiming.wrutils.test.blockstatetest.BlockStatus;

public class test1 {
    public static void main(String[] args) {
        BlockHistoryManager manager = new BlockHistoryManager();

        // 记录多个时刻的方块信息
        manager.recordBlockState(new BlockStatus(0, 1, 0, "stone", 0, 1));
        manager.recordBlockState(new BlockStatus(0, 1, 0, "grass", 0, 2));
        manager.recordBlockState(new BlockStatus(0, 1, 0, "dirt", 0, 3));

        manager.recordBlockState(new BlockStatus(1, 1, 1, "wood", 0, 2));
        manager.recordBlockState(new BlockStatus(2, 0, 0, "water", 0, 2));

        // 查询 (0,1,0) 的历史记录
        System.out.println("位置 (0,1,0) 的历史");
        for (BlockStatus state : manager.getBlockHistory(0, 1, 0)) {
            System.out.println("Time: " + state.timestamp + ", Type: " + state.type);
        }

        // 查询时刻 2 的所有方块
        System.out.println("\n【时刻 2 的所有方块】");
        for (BlockStatus state : manager.getBlockStatesAt(2)) {
            System.out.println("Pos: (" + state.x + "," + state.y + "," + state.z + ") Type: " + state.type);
        }


        System.out.println("Pos(0,1,0) "+manager.getBlockHistory(0,1,0).get(1).type +"  "+ manager.getBlockHistory(0,1,0).get(1));
        BlockStatus blockStatus = manager.getBlockHistory(0,1,0).get(1);
        blockStatus.type = "test";
        System.out.println("Pos(0,1,0) "+manager.getBlockHistory(0,1,0).get(1).type +"  "+ manager.getBlockHistory(0,1,0).get(1));

        System.out.println("timestamp:2 "+manager.getBlockStatesAt(2).get(0).type +"  "+ manager.getBlockStatesAt(2).get(0));
//        System.out.println("timestamp:2 "+manager.getBlockStatesAt(2).get(1).type+manager.getBlockStatesAt(2).get(1));
    }
}
