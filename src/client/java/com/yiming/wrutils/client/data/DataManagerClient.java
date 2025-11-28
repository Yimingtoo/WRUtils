package com.yiming.wrutils.client.data;

import com.yiming.wrutils.client.ModInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;

import java.io.File;
import java.nio.file.Path;

public class DataManagerClient {
    public static File getDataDir() {
        return new File(new File(MinecraftClient.getInstance().runDirectory, "config"), ModInfo.MOD_ID);
    }

    public static String getWorldFolderName() {
        MinecraftClient client = MinecraftClient.getInstance();
        MinecraftServer server = client.getServer();
        String worldFolderName = "null";

        if (server != null) {
            Path worldDir = server.getSavePath(WorldSavePath.ROOT);
            worldFolderName = worldDir.getFileName().toString();
//            System.out.println("当前世界文件夹名称: " + worldFolderName);
        }
        File file = new File(".");
        return "";
    }

    public static void save() {
        // 保存数据包括
        //
        // 1. 区域选择数据，保存在getDataDir文件目录下
        // 每个AreaGroup对应一个json文件
        // 每个AreaList包含多个SubArea
        System.out.println("save：" + DataManagerClient.getDataDir().getAbsolutePath());

    }
}
