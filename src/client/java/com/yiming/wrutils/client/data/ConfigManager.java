package com.yiming.wrutils.client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Deprecated
public class ConfigManager {
    private Properties properties;
    private File configFile;

    public ConfigManager(String configFilePath) {
        properties = new Properties();
        configFile = new File(configFilePath);
        loadConfig();
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 如果配置文件不存在，创建一个并设置默认值
            setConfigOption("option1", true);
            saveConfig();
        }
    }

    public void saveConfig() {
        // 如果配置文件所在文件夹不存在，创建文件夹
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(configFile)) {

            properties.store(fos, "Minecraft Mod Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getConfigOption(String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }

    public void setConfigOption(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void setConfigOptionAndSave(String key, boolean value) {
        setConfigOption(key, value);
        saveConfig();

    }
}
