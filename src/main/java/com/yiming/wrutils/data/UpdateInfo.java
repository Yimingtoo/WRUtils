package com.yiming.wrutils.data;

import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;

import java.util.HashSet;
import java.util.Set;

public class UpdateInfo {

    private static final Set<RedstoneInfo> blocks = new HashSet<>();

    private static final Set<BlockPos> checkedBlockPoses = new HashSet<>();

    public static boolean isBlockChecked(BlockPos pos) {
        return checkedBlockPoses.contains(pos);
    }


    public static void init() {
        checkedBlockPoses.add(new BlockPos(0, 0, 0));
    }

    public static void addCheckedBlockPos(BlockPos pos) {
        checkedBlockPoses.add(pos);
    }

    public static void removeCheckedBlockPos(BlockPos pos) {
        if (!checkedBlockPoses.contains(pos)) {
            return;
        }
        checkedBlockPoses.remove(pos);
    }

    public static void addBlock(RedstoneInfo block) {
        blocks.add(block);
    }
}
