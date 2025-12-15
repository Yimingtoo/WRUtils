package com.yiming.wrutils.client.input;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

/**
 * 每添加一个快捷键，必须在添加到 HOTKEY_LIST 中和 KeyCallbacks 添加相应回调
 */
public class HotkeysManagement {
    private static final String HOTKEYS_CATEGORY = "wrutils.config.hotkeys";

    public static final ConfigHotkey OPEN_MAIN_MENU_SCREEN = new ConfigHotkey("openMainMenuScreen", "I", KeybindSettings.RELEASE_EXCLUSIVE).apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey OPEN_AREA_GROUP_SCREEN = new ConfigHotkey("openAreaGroupScreen", "").apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey OPEN_AREA_LIST_SCREEN = new ConfigHotkey("openAreaListScreen", "").apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey OPEN_SUB_AREA_CONFIG_SCREEN = new ConfigHotkey("openSubAreaScreen", "").apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey OPEN_CONFIGS_SCREEN = new ConfigHotkey("openConfigsScreen", "I,C").apply(HOTKEYS_CATEGORY);

    public static final ConfigHotkey SELECT_MOVE_CTRL = new ConfigHotkey("selectMoveCtrl", "LEFT_ALT", KeybindSettings.MODIFIER_INGAME).apply(HOTKEYS_CATEGORY);

    public static final ConfigHotkey TOOL_PLACE_CORNER_1 = new ConfigHotkey("toolPlaceCorner1", "BUTTON_1", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey TOOL_PLACE_CORNER_2 = new ConfigHotkey("toolPlaceCorner2", "BUTTON_2", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey TOOL_SELECT_ELEMENTS = new ConfigHotkey("toolSelectElements", "BUTTON_3", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey PREVIOUS_EVENT = new ConfigHotkey("previousEvent", "UP", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY);
    public static final ConfigHotkey NEXT_EVENT = new ConfigHotkey("nextEvent", "DOWN", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY);

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_MAIN_MENU_SCREEN,
            OPEN_AREA_GROUP_SCREEN,
            OPEN_AREA_LIST_SCREEN,
            OPEN_SUB_AREA_CONFIG_SCREEN,
            OPEN_CONFIGS_SCREEN,
            SELECT_MOVE_CTRL,
            TOOL_PLACE_CORNER_1,
            TOOL_PLACE_CORNER_2,
            TOOL_SELECT_ELEMENTS,
            PREVIOUS_EVENT,
            NEXT_EVENT
    );
}
