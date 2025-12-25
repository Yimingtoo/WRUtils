package com.yiming.wrutils.client.data;

import com.yiming.wrutils.client.ModInfo;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterTypeList;
import com.yiming.wrutils.client.gui.widget.filter.item.ItemType;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataManagerClient {
    public static Item debugItem = Items.WOODEN_SWORD;

    public static boolean isFilterEventRender;
    public static ArrayList<BaseEvent> filterEventList = new ArrayList<>();
    public static int filterEventPointer;

    public static FilterTypeList filterTypeList = new FilterTypeList();

    public static long eventOriginTick;


    public static void clearEvents() {
        DataManager.eventRecorder.clear();
        filterEventList.clear();
        filterEventPointer = 0;
    }

    public static void resetFilter() {
        filterEventPointer = 0;
        filterEventList.clear();
        filterEventList.addAll(DataManager.eventRecorder);
    }

    public static BaseEvent getFilterEventAtPointer() {
        if (filterEventPointer >= filterEventList.size()) {
            return null;
        }
        return filterEventList.get(filterEventPointer);
    }

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
