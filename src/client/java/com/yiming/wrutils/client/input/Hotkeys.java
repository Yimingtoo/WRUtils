package com.yiming.wrutils.client.input;

import com.google.common.collect.ImmutableList;
import com.yiming.wrutils.client.utils.MemberRegistry;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.lang.reflect.Member;
import java.util.List;

/**
 * 每添加一个快捷键，必须在添加到 HOTKEY_LIST 中和 KeyCallbacks 添加相应回调
 */
public class Hotkeys  {
    private static final String HOTKEYS_CATEGORY = "wrutils.client.input.hotkeys";

    private static final ImmutableList.Builder<ConfigHotkey> builder = ImmutableList.builder();
    public static final List<ConfigHotkey> HOTKEY_LIST;

    public static final ConfigHotkey OPEN_MAIN_MENU_SCREEN = register(new ConfigHotkey("openMainMenuScreen", "I", KeybindSettings.RELEASE_EXCLUSIVE).apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey OPEN_AREA_GROUP_SCREEN = register(new ConfigHotkey("openAreaGroupScreen", "").apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey OPEN_AREA_LIST_SCREEN = register(new ConfigHotkey("openAreaListScreen", "").apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey OPEN_SUB_AREA_CONFIG_SCREEN = register(new ConfigHotkey("openSubAreaScreen", "").apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey OPEN_CONFIGS_SCREEN = register(new ConfigHotkey("openConfigsScreen", "I,C").apply(HOTKEYS_CATEGORY));

    public static final ConfigHotkey SELECT_MOVE_CTRL = register(new ConfigHotkey("selectMoveCtrl", "LEFT_ALT", KeybindSettings.MODIFIER_INGAME).apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey TOOL_PLACE_CORNER_1 = register(new ConfigHotkey("toolPlaceCorner1", "BUTTON_1", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey TOOL_PLACE_CORNER_2 = register(new ConfigHotkey("toolPlaceCorner2", "BUTTON_2", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey TOOL_SELECT_ELEMENTS = register(new ConfigHotkey("toolSelectElements", "BUTTON_3", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));

    public static final ConfigHotkey PREVIOUS_EVENT = register(new ConfigHotkey("previousEvent", "UP", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));
    public static final ConfigHotkey NEXT_EVENT = register(new ConfigHotkey("nextEvent", "DOWN", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));

    public static final ConfigHotkey CLEAR_EVENTS = register(new ConfigHotkey("nextEvent", "I,PERIOD", KeybindSettings.PRESS_ALLOWEXTRA).apply(HOTKEYS_CATEGORY));

    private static ConfigHotkey register(ConfigHotkey configHotkey) {
        builder.add(configHotkey);
        return configHotkey;
    }

    // Warning: 必须放在类的最后
    // DO NOT MOVE THIS STATIC BLOCK
    // It must remain after all static ConfigHotkey fields
    static {
        HOTKEY_LIST = builder.build();
    }
}
