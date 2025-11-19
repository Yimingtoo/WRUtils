package com.yiming.wrutils.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CustomTextFieldWidget extends TextFieldWidget {
    Runnable lostFocusAction;

    public CustomTextFieldWidget(TextRenderer textRenderer, int width, int height, Text text) {
        super(textRenderer, width, height, text);
    }

    public void setLostFocusAction(Runnable action) {
        this.lostFocusAction = action;
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            ((TextFieldWidget) this).setFocused(false);

        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            if (this.lostFocusAction != null) {
                this.lostFocusAction.run();
            }
        }
    }
}
