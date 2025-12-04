package com.yiming.wrutils.client.gui.widget.search;

import com.yiming.wrutils.client.gui.widget.search.clickable.BaseClickableWidget;
import com.yiming.wrutils.client.gui.widget.search.dropdown.DropDownSelectListWidget;
import com.yiming.wrutils.client.gui.widget.search.dropdown.DropDownSingleSelectListWidget;
import com.yiming.wrutils.client.gui.widget.search.dropdown.DropDownTextFieldListWidget;
import com.yiming.wrutils.client.utils.WrutilsColor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class SearchWidget extends ClickableWidget {
    //    private final ItemListWidget dropDownWidget;
//    private final DropDownSelectListWidget tickSequenceSelectWidget;
//    private final DropDownTextFieldListWidget gameTickSelectWidget;
//    private final DropDownSelectListWidget areaListSelectListWidget;
    private final HashMap<TabButton, HashMap<String, BaseClickableWidget>> widgetMap = new HashMap<>();

    private boolean isOpen = false;
    private final BaseClickableWidget switchButton;
    private final TabButton timeButton;
    private final TabButton positionButton;
    private final TabButton eventButton;

    private BaseClickableWidget currentWidget = null;


    public SearchWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
        ArrayList<String> list = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape", "Honeydew", "Iceberg"));
//        list = new ArrayList<>(List.of("Apple"));


        int x1 = 10;
        this.switchButton = new BaseClickableWidget(x + x1, y, 18, 18, Text.of("S"), this::switchButtonOnClick);
        x1 += 23;
        this.timeButton = new TabButton(x + x1, y, 80, 18, Text.of("Time"), this::timeButtonOnClick);
        x1 += 85;
        this.positionButton = new TabButton(x + x1, y, 80, 18, Text.of("Position"), this::positionButtonOnClick);
        x1 += 85;
        this.eventButton = new TabButton(x + x1, y, 80, 18, Text.of("Event"), this::eventButtonOnClick);


//        this.gameTickSelectWidget = new DropDownTextFieldListWidget(10, y + 23, 200, 100, 18, 18, Text.of("DropDownText"), list);
//        this.tickSequenceSelectWidget = new DropDownSelectListWidget(115, y + 23, 100, 100, 18, 18, Text.of("DropDown"), list);
//        this.areaListSelectListWidget = new DropDownSelectListWidget(10, y + 23, 100, 100, 18, 18, Text.of("Area"), list);

        this.widgetMap.put(this.timeButton, new HashMap<>());
        this.widgetMap.get(this.timeButton).put("GameTick", new DropDownTextFieldListWidget(10, y + 23, 200, 100, 18, 18, Text.of("Game Tick"), list));
        this.widgetMap.get(this.timeButton).put("TickSequence", new DropDownSelectListWidget(115, y + 23, 100, 100, 18, 18, Text.of("Sequence"), list));

        this.widgetMap.put(this.positionButton, new HashMap<>());
        this.widgetMap.get(this.positionButton).put("AreaSelect", new DropDownSingleSelectListWidget(10, y + 23, 100, 100, 18, 18, Text.of("Area"), list));

        this.widgetMap.put(this.eventButton, new HashMap<>());

        this.enableSubWidgets(null);

    }

    public void switchButtonOnClick() {
        if (this.isOpen) {
            this.isOpen = false;
            this.enableSubWidgets(null);
        } else {
            this.isOpen = true;
            if (this.currentWidget == null) {
                this.currentWidget = this.timeButton;
            }
            this.enableSubWidgets(this.currentWidget);
        }
    }

    public void timeButtonOnClick() {
        this.isOpen = true;
        this.currentWidget = this.timeButton;
        this.enableSubWidgets(this.timeButton);
        this.currentWidget = this.timeButton;
    }

    public void positionButtonOnClick() {
        this.isOpen = true;
        this.enableSubWidgets(this.positionButton);
        this.currentWidget = this.positionButton;
    }


    public void eventButtonOnClick() {
        this.isOpen = true;
        this.enableSubWidgets(this.eventButton);
        this.currentWidget = this.eventButton;
    }

    public void enableSubWidgets(@Nullable BaseClickableWidget widget) {
        this.widgetMap.forEach((mainWidget, widgetMap1) -> {
            boolean bl = mainWidget == widget;
            mainWidget.setTabSelected(bl);
            for (BaseClickableWidget subWidget : widgetMap1.values()) {
                subWidget.setVisibleAndActive(bl);
            }
        });
    }

    private boolean handleMouseEvent(Function<Element, Boolean> handler) {
        boolean result = false;
        if (handler.apply(this.switchButton)) result = true;
        if (handler.apply(this.timeButton)) result = true;
        if (handler.apply(this.positionButton)) result = true;
        if (handler.apply(this.eventButton)) result = true;
        for (HashMap<String, BaseClickableWidget> widgetMap1 : this.widgetMap.values()) {
            for (BaseClickableWidget widget : widgetMap1.values()) {
                if (handler.apply(widget)) result = true;
            }
        }
        return result;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

        if (this.isOpen) {
            context.fill(this.getX() + 5, this.getY() + 17, this.getX() + this.getWidth() - 5, this.getY() + 18 + 30, WrutilsColor.GREY_0);
            context.fill(this.getX() + 5 + 1, this.getY() + 17 + 1, this.getX() + this.getWidth() - 5 - 1, this.getY() + 18 + 30 - 1, WrutilsColor.BLACK);
        }

        this.switchButton.render(context, mouseX, mouseY, delta);
        this.timeButton.render(context, mouseX, mouseY, delta);
        this.positionButton.render(context, mouseX, mouseY, delta);
        this.eventButton.render(context, mouseX, mouseY, delta);
        for (HashMap<String, BaseClickableWidget> widgetMap1 : this.widgetMap.values()) {
            for (BaseClickableWidget widget : widgetMap1.values()) {
                widget.render(context, mouseX, mouseY, delta);
            }
        }


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

    public static class TabButton extends BaseClickableWidget {
        boolean isTabSelected = false;

        public TabButton(int x, int y, int width, int height, Text message, Runnable onClickAction) {
            super(x, y, width, height, message, onClickAction);
        }

        public void setTabSelected(boolean flag) {
            this.isTabSelected = flag;
        }

        public boolean isTabSelected() {
            return this.isTabSelected;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 5, this.getY() + 5, Colors.WHITE);
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), WrutilsColor.GREY_0);
            context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - (this.isTabSelected ? 0 : 1), this.isTabSelected ? WrutilsColor.BLACK : WrutilsColor.GREY_1);
        }

    }
}
