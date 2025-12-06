package com.yiming.wrutils.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.function.Function;

public class CustomTextFieldWidget extends TextFieldWidget {
    Function<String, Boolean> lostFocusFunction;

    public CustomTextFieldWidget(TextRenderer textRenderer, int width, int height, Text text) {
        super(textRenderer, width, height, text);
    }

    public void setLostFocusFunction(Function<String, Boolean> function) {
        this.lostFocusFunction = function;
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
        if (this.isFocused() && !focused) {
            if (this.lostFocusFunction != null) {
                if (!this.lostFocusFunction.apply(this.getText())) {
                    return;
                }

            }
        }
        super.setFocused(focused);
    }
}
