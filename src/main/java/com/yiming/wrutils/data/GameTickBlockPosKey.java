package com.yiming.wrutils.data;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class GameTickBlockPosKey {
    private final long gameTick;
    private final long blockPos;

    public GameTickBlockPosKey(long gameTick, BlockPos blockPos) {
        this.gameTick = gameTick;
        this.blockPos = blockPos.asLong();
    }
    public GameTickBlockPosKey(long gameTick, long blockPos) {
        this.gameTick = gameTick;
        this.blockPos = blockPos;
    }

    // 重写 equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameTickBlockPosKey that = (GameTickBlockPosKey) o;
        return gameTick == that.gameTick && blockPos == that.blockPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameTick, blockPos);
    }
}
