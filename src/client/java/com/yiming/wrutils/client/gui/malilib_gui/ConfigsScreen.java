package com.yiming.wrutils.client.gui.malilib_gui;

import com.yiming.wrutils.client.ModInfo;
import com.yiming.wrutils.client.input.HotkeysManagement;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;

import java.util.List;
import java.util.Objects;


public class ConfigsScreen extends GuiConfigsBase {
    private static Tabs currentTab = Tabs.GENERIC;

    public ConfigsScreen() {
        super(10, 50, ModInfo.MOD_ID, null, ModInfo.MOD_ID + ".gui.title.malilib_gui.configs_screen", ModInfo.MOD_VERSION);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, "Generic");
        button.setEnabled(currentTab != Tabs.GENERIC);
        this.addButton(button, (buttonBase, mouseButton) -> {
            this.setButtonListener(Tabs.GENERIC);
        });

        ButtonGeneric button1 = new ButtonGeneric(x + button.getWidth() + 2, y, -1, 20, "Hotkeys");
        button1.setEnabled(currentTab != Tabs.HOTKEYS);
        this.addButton(button1, (buttonBase, mouseButton) -> {
            this.setButtonListener(Tabs.HOTKEYS);
        });

    }

    private void setButtonListener(Tabs tab) {
        currentTab = tab;
        this.reCreateListWidget();
        Objects.requireNonNull(this.getListWidget()).resetScrollbarPosition();
        this.initGui();
    }

    @Override
    protected boolean useKeybindSearch() {
        return false;
    }

    @Override
    protected void onSettingsChanged() {
        super.onSettingsChanged();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<? extends IConfigBase> configs = switch (currentTab) {
            case GENERIC -> List.of();
            case HOTKEYS -> HotkeysManagement.HOTKEY_LIST;
        };
        return ConfigOptionWrapper.createFor(configs);
    }

    public enum Tabs {
        GENERIC,
        HOTKEYS
    }
}
