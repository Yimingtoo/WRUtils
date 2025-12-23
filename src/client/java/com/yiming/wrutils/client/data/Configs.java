package com.yiming.wrutils.client.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yiming.wrutils.client.ModInfo;
import com.yiming.wrutils.client.data.configs.Generic;
import com.yiming.wrutils.client.input.Hotkeys;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Files;
import java.nio.file.Path;

public class Configs implements IConfigHandler {
    public static final String CONFIG_FILE_NAME = ModInfo.MOD_ID + ".json";

    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);
        if (!Files.exists(configFile) || !Files.isReadable(configFile)) {
            return;
        }

        JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);
        if (element == null || !element.isJsonObject()) {
            System.out.println("Error parsing config file at: " + configFile.toAbsolutePath().toString());
            return;
        }

        JsonObject root = element.getAsJsonObject();
        ConfigUtils.readConfigBase(root, "Generic", Generic.GENERIC_LIST);
        ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
    }

    @Override
    public void save() {
        Path dir = FileUtils.getConfigDirectoryAsPath();
        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (!Files.isDirectory(dir)) {
            System.out.println("Could not create config directory: " + dir.toAbsolutePath());
            return;
        }

        JsonObject root = new JsonObject();
        ConfigUtils.writeConfigBase(root, "Generic", Generic.GENERIC_LIST);
        ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEY_LIST);
        JsonUtils.writeJsonToFileAsPath(root, dir.resolve(CONFIG_FILE_NAME));
    }
}
