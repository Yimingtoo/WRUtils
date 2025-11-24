package com.yiming.wrutils.client.input;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.data.selected_area.SelectBox;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class MouseManagement implements IMouseInputHandler {
    private static final MouseManagement INSTANCE = new MouseManagement();

    public static MouseManagement getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean onMouseScroll(int mouseX, int mouseY, double dWheel) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (GuiUtils.getCurrentScreen() == null && client.world != null && client.player != null) {
            return this.handleMouseScroll(dWheel);
        }

        return false;
    }

    private boolean handleMouseScroll(double dWheel) {
        Entity entity = EntityUtils.getCameraEntity();
        if (HotkeysManagement.SELECT_MOVE_CTRL.getKeybind().isKeybindHeld()) {
            final int amount = dWheel > 0 ? 1 : -1;

            SelectBox box = Wrutils.getCurrentSelectBox();
            if (box != null) {
                Wrutils.getCurrentSelectBox().moveBox(entity, amount);
            }
            return true;
        }
        return false;
    }
}
