package com.yiming.wrutils.client.gui.widget.search;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchWidget extends ClickableWidget {
    //    private final ItemListWidget dropDownWidget;
    private final DropDownSelectListWidget dropDownSelectListWidget;
    private final DropDownTextFieldListWidget dropDownTextFieldListWidget;;

    public SearchWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        ArrayList<String> list = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape", "Honeydew", "Iceberg"));
        this.dropDownSelectListWidget = new DropDownSelectListWidget(10, y, 100, 100, 18, 18, Text.of("DropDown"), list);

        this.dropDownTextFieldListWidget = new DropDownTextFieldListWidget(115, y, 100, 100, 18, 18, Text.of("DropDownText"), list);

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.dropDownSelectListWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.dropDownSelectListWidget.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        // Do nothing when blank place is clicked
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = this.dropDownSelectListWidget.mouseReleased(mouseX, mouseY, button);
        return bl;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = this.dropDownSelectListWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return bl;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.dropDownSelectListWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }

        return false;
    }
}
