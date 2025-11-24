package com.yiming.wrutils.client.input;

import com.yiming.wrutils.client.ModInfo;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;

public class KeyBoardManagement implements IKeybindProvider, IKeyboardInputHandler {
    private static final KeyBoardManagement INSTANCE = new KeyBoardManagement();

    public static KeyBoardManagement getInstance() {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager iKeybindManager) {
        for (IHotkey hotkey : HotkeysManagement.HOTKEY_LIST) {
            iKeybindManager.addKeybindToMap(hotkey.getKeybind());
        }

    }

    @Override
    public void addHotkeys(IKeybindManager iKeybindManager) {
        iKeybindManager.addHotkeysForCategory(ModInfo.MOD_NAME, ModInfo.MOD_ID + ".hotkeys.category.generic_hotkeys", HotkeysManagement.HOTKEY_LIST);
    }

}
