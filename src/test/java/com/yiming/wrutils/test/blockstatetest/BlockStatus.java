package com.yiming.wrutils.test.blockstatetest;

public class BlockStatus {
    public int x, y, z;        // 坐标
    public String type;        // 方块类型
    public int meta;           // 元数据（可选）
    public long timestamp;     // 时间戳（如 tick、秒等）

    public BlockStatus(int x, int y, int z, String type, int meta, long timestamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.meta = meta;
        this.timestamp = timestamp;
    }

    // Getters...
}
