package com.yiming.wrutils.client.gui.widget.search;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchWidget extends ClickableWidget {
    //    private final ItemListWidget dropDownWidget;
    private final DropDownSelectListWidget dropDownSelectListWidget;
    private final DropDownTextFieldListWidget dropDownTextFieldListWidget;
    ;

    public SearchWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        ArrayList<String> list = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape", "Honeydew", "Iceberg"));
        this.dropDownTextFieldListWidget = new DropDownTextFieldListWidget(10, y, 200, 100, 100, 18, 18, Text.of("DropDownText"), list);
        this.dropDownSelectListWidget = new DropDownSelectListWidget(115, y, 100, 100, 18, 18, Text.of("DropDown"), list);


    }

    private boolean handleMouseEvent(Function<Element, Boolean> handler) {
        boolean result = false;
        if (handler.apply(this.dropDownTextFieldListWidget)) result = true;
        if (handler.apply(this.dropDownSelectListWidget)) result = true;

        return result;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.dropDownSelectListWidget.render(context, mouseX, mouseY, delta);
        this.dropDownTextFieldListWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseClicked(mouseX, mouseY, button));
        return bl;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseReleased(mouseX, mouseY, button));
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = handleMouseEvent(widget -> widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean bl = handleMouseEvent(widget -> widget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        return bl;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        boolean bl = handleMouseEvent(widget -> widget.charTyped(chr, keyCode));
        return bl || super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = handleMouseEvent(widget -> widget.keyPressed(keyCode, scanCode, modifiers));
        return bl || super.keyPressed(keyCode, scanCode, modifiers);
    }
}
