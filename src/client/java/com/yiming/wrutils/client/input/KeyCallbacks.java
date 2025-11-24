package com.yiming.wrutils.client.input;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.Notification;
import com.yiming.wrutils.client.WrutilsClient;
import com.yiming.wrutils.client.WrutilsClientUtils;
import com.yiming.wrutils.client.gui.AreaGroupScreen;
import com.yiming.wrutils.client.gui.AreaListScreen;
import com.yiming.wrutils.client.gui.MainMenuScreen;
import com.yiming.wrutils.client.gui.SubAreaScreen;
import com.yiming.wrutils.client.gui.malilib_gui.ConfigsScreen;
import com.yiming.wrutils.data.selected_area.SelectBox;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

public class KeyCallbacks {

    public static void keyCallBackInit(MinecraftClient client) {
        IHotkeyCallback callbackHotkeys = new HotKeyCallback(client);

        HotkeysManagement.OPEN_MAIN_MENU_SCREEN.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.OPEN_AREA_GROUP_SCREEN.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.OPEN_AREA_LIST_SCREEN.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.OPEN_SUB_AREA_CONFIG_SCREEN.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.OPEN_CONFIGS_SCREEN.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.SELECT_MOVE_CTRL.getKeybind().setCallback(callbackHotkeys);

        HotkeysManagement.TOOL_PLACE_CORNER_1.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.TOOL_PLACE_CORNER_2.getKeybind().setCallback(callbackHotkeys);
        HotkeysManagement.TOOL_SELECT_ELEMENTS.getKeybind().setCallback(callbackHotkeys);

    }

    private static class HotKeyCallback implements IHotkeyCallback {
        private final MinecraftClient client;

        public HotKeyCallback(MinecraftClient client) {
            this.client = client;
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (this.client.player == null || this.client.world == null) {
                return false;
            }

            // 处理鼠标按键事件
            boolean bl1 = key == HotkeysManagement.TOOL_PLACE_CORNER_1.getKeybind();
            boolean bl2 = key == HotkeysManagement.TOOL_PLACE_CORNER_2.getKeybind();
            boolean bl3 = key == HotkeysManagement.TOOL_SELECT_ELEMENTS.getKeybind();
            boolean blItem = client.player.getMainHandStack().getItem() == WrutilsClient.debugItem;
            if (blItem) {
                if (bl1 || bl2) {
                    WrutilsClientUtils.setSelectBoxCorner(client, bl1 ? SelectBox.SelectedCorner.CORNER_1 : SelectBox.SelectedCorner.CORNER_2);
                } else if (bl3) {
                    WrutilsClientUtils.selectElement(client);
                }
            }


            if (key == HotkeysManagement.OPEN_MAIN_MENU_SCREEN.getKeybind()) {
                this.client.setScreen(new MainMenuScreen());
                return true;
            } else if (key == HotkeysManagement.OPEN_AREA_GROUP_SCREEN.getKeybind()) {
                this.client.setScreen(new AreaGroupScreen(null));
                return true;
            } else if (key == HotkeysManagement.OPEN_AREA_LIST_SCREEN.getKeybind()) {
                if (Wrutils.getCurrentBoxes() == null) {
                    Notification.addNotification("No Area is Selected!!!", 1000);
                    return false;
                }
                this.client.setScreen(new AreaListScreen(null, Wrutils.getCurrentBoxes()));
                return true;
            } else if (key == HotkeysManagement.OPEN_SUB_AREA_CONFIG_SCREEN.getKeybind()) {
                if (Wrutils.getCurrentSelectBox() == null) {
                    Notification.addNotification("No Sub Area is Selected!!!", 1000);
                    return false;
                }
                this.client.setScreen(new SubAreaScreen(null, Wrutils.getCurrentBoxes(), Wrutils.getCurrentSelectBox()));
                return true;
            } else if (key == HotkeysManagement.OPEN_CONFIGS_SCREEN.getKeybind()) {
                GuiBase.openGui(new ConfigsScreen());
                return true;
            }


            return false;
        }
    }

}
