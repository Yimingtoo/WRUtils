package com.yiming.wrutils.client.gui.widget.filter;

import com.yiming.wrutils.client.gui.widget.filter.clickable.BaseClickableWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.*;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemTextFieldListWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.SingleSelectItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.*;
import com.yiming.wrutils.client.gui.widget.filter.item.block.SourceBlockItem;
import com.yiming.wrutils.client.gui.widget.filter.item.block.TargetBlockItem;
import com.yiming.wrutils.client.gui.widget.filter.item.bool_item.BooleanItem;
import com.yiming.wrutils.client.gui.widget.filter.item.bool_item.ScheduledTickAddedStatusItem;
import com.yiming.wrutils.client.gui.widget.filter.item.int_item.DelayItem;
import com.yiming.wrutils.client.gui.widget.filter.item.int_item.IntegerItem;
import com.yiming.wrutils.client.gui.widget.filter.item.long_item.LongItem;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.ScheduledTickAddEvent;
import com.yiming.wrutils.data.event.ScheduledTickExecEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class FilterWidget extends ClickableWidget {
    private final HashMap<TabButton, HashMap<String, ExpandableClickableWidget>> widgetMap = new HashMap<>();

    private boolean isOpen = false;
    private final BaseClickableWidget switchButton;
    private final TabButton timeButton;
    private final TabButton positionButton;
    private final TabButton eventButton;

    private BaseClickableWidget currentWidget = null;

    private Runnable onOpenStateChangeAction;


    public FilterWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);

        int x1 = 10;
        this.switchButton = new BaseClickableWidget(x + x1, y, 18, 18, Text.of("S"), this::switchButtonOnClick) {
            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawTextWithShadow(this.textRenderer, this.getMessage(), this.getX() + 6, this.getY() + 5, Colors.WHITE);
                context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), WrutilsColor.GREY_0);
                context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, WrutilsColor.BLACK);
            }
        };

        x1 += 23;
        this.timeButton = new TabButton(x + x1, y, 80, 18, Text.of("Time"), this::timeButtonOnClick);
        x1 += 85;
        this.positionButton = new TabButton(x + x1, y, 80, 18, Text.of("Position"), this::positionButtonOnClick);
        x1 += 85;
        this.eventButton = new TabButton(x + x1, y, 80, 18, Text.of("Event"), this::eventButtonOnClick);

        this.widgetMap.put(this.timeButton, new HashMap<>());
//        ArrayList<GameTickItem> gameTickItems = new ArrayList<>();
        DropDownTextFieldListWidget gameTickSelectWidget = new DropDownTextFieldListWidget(10, y + 23, 100, 18, 200, 18, Text.of("Game Tick"), new ArrayList<>());
        // 判断text是否为数字
        gameTickSelectWidget.getItemTextFieldListWidget().setHeaderTextFieldLostFocusFunction((text) -> text.trim().matches("\\d+"));
        // 判断是否为单个整数 || 判断是否为两个整数用 '-' 分割
        gameTickSelectWidget.getItemTextFieldListWidget().setItemTextFieldLostFocusFunction((text) -> {
            return text.trim().matches("\\d+") || text.trim().matches("\\d+\\s*-\\s*\\d+");
        });
        if (!DataManager.eventRecorder.isEmpty()) {
            ItemTextFieldListWidget.ItemHeaderTextFieldEntry entry = gameTickSelectWidget.getItemTextFieldListWidget().getHeaderEntry();
            ((LongItem) (entry.getItem())).setValue(DataManager.eventRecorder.getFirst().getTimeStamp().gameTime());
            entry.updateTextField();
        }
        this.widgetMap.get(this.timeButton).put("GameTick", gameTickSelectWidget);
        this.widgetMap.get(this.timeButton).put("TickSequence", new DropDownSelectListWidget(115, y + 23, 100, 18, 100, 18, Text.of("Sequence"), SequenceItem.SequenceItems()));

        this.widgetMap.put(this.positionButton, new HashMap<>());
        // 获得 DataManager.eventRecorder 中包含的方块Block
        HashSet<Block> targetBlockSet = new HashSet<>();
        HashSet<Block> sourceBlockSet = new HashSet<>();
        HashSet<Integer> delaySet = new HashSet<>();
        for (BaseEvent event : DataManager.eventRecorder) {
            if (event.getTargetBlockInfo().state() != null) {
                targetBlockSet.add(event.getTargetBlockInfo().state().getBlock());
            }
            if (event.getSourceBlockInfo().state() != null) {
                sourceBlockSet.add(event.getSourceBlockInfo().state().getBlock());
            }
            if (event instanceof ScheduledTickAddEvent event1) {
                delaySet.add(event1.getDelay());
            }
            if (event instanceof ScheduledTickExecEvent event1) {
                delaySet.add(event1.getDelay());
            }
        }
        DropDownSingleSelectListWidget targetAreaWidget = new DropDownSingleSelectListWidget(10, y + 23, 90, 18, 200, 18, Text.of("Target Area"), AreaListItem.getAreaListItems(DataManager.areaGroupManagement.getList()));
        SingleSelectItemListWidget w1 = (SingleSelectItemListWidget) targetAreaWidget.getItemListWidget();
        w1.setHeaderItemEntry(new ItemListWidget.HeaderItemEntry());
        w1.setSingleCheckedItem((ItemListWidget.ItemEntry) w1.getFirst());
        this.widgetMap.get(this.positionButton).put("TargetArea", targetAreaWidget);
        this.widgetMap.get(this.positionButton).put("TargetBlock", new DropDownSelectListWidget(105, y + 23, 90, 18, 100, 18, Text.of("Target Block"), TargetBlockItem.getBlockItems(targetBlockSet)));
        DropDownSingleSelectListWidget sourceAreaWidget = new DropDownSingleSelectListWidget(210, y + 23, 90, 18, 200, 18, Text.of("Source Area"), AreaListItem.getAreaListItems(DataManager.areaGroupManagement.getList()));
        SingleSelectItemListWidget w2 = (SingleSelectItemListWidget) sourceAreaWidget.getItemListWidget();
        w2.setHeaderItemEntry(new ItemListWidget.HeaderItemEntry());
        w2.setSingleCheckedItem((ItemListWidget.ItemEntry) w2.getFirst());
        this.widgetMap.get(this.positionButton).put("SourceArea", sourceAreaWidget);
        this.widgetMap.get(this.positionButton).put("SourceBlock", new DropDownSelectListWidget(305, y + 23, 90, 18, 100, 18, Text.of("Source Block"), SourceBlockItem.getBlockItems(sourceBlockSet)));

        this.widgetMap.put(this.eventButton, new HashMap<>());
        this.widgetMap.get(this.eventButton).put("EventType", new DropDownSelectListWidget(10, y + 23, 95, 18, 200, 18, Text.of("Event Type"), EventTypeItem.EventTypes()));

        this.widgetMap.get(this.eventButton).put("Delay", new DropDownSelectListWidget(110, y + 23, 95, 18, 200, 18, Text.of("Delay"), DelayItem.getDelayItems(delaySet)));
        this.widgetMap.get(this.eventButton).put("Priority", new DropDownSelectListWidget(210, y + 23, 95, 18, 200, 18, Text.of("Priority"), PriorityItem.priorityItems()));
        this.widgetMap.get(this.eventButton).put("IsAdded", new DropDownSelectListWidget(310, y + 23, 95, 18, 200, 18, Text.of("Is Added"), ScheduledTickAddedStatusItem.getScheduledTickAddedStatusItems()));

        this.enableSubWidgets(null);

    }

    public ArrayList<ArrayList<FilterType<?>>> getFilterItems() {
        ArrayList<ArrayList<FilterType<?>>> list = new ArrayList<>();
        // Time Button
        HashMap<String, ExpandableClickableWidget> map1 = this.widgetMap.get(this.timeButton);
        DropDownTextFieldListWidget gameTickWidget = (DropDownTextFieldListWidget) map1.get("GameTick");
        list.add(gameTickWidget.getFilterItemList());

        DropDownSelectListWidget tickSequenceWidget = (DropDownSelectListWidget) map1.get("TickSequence");
        list.add(tickSequenceWidget.getFilterItemList());

        // Position Button
        HashMap<String, ExpandableClickableWidget> map2 = this.widgetMap.get(this.positionButton);
        DropDownSingleSelectListWidget targetAreaWidget = (DropDownSingleSelectListWidget) map2.get("TargetArea");

        DropDownSelectListWidget targetBlockWidget = (DropDownSelectListWidget) map2.get("TargetBlock");
        list.add(targetBlockWidget.getFilterItemList());
        DropDownSingleSelectListWidget sourceAreaWidget = (DropDownSingleSelectListWidget) map2.get("SourceArea");
        DropDownSelectListWidget sourceBlockWidget = (DropDownSelectListWidget) map2.get("SourceBlock");
        list.add(sourceBlockWidget.getFilterItemList());


        // Event Button
        HashMap<String, ExpandableClickableWidget> map3 = this.widgetMap.get(this.eventButton);
        DropDownSelectListWidget eventTypeWidget = (DropDownSelectListWidget) map3.get("EventType");
        list.add(eventTypeWidget.getFilterItemList());
        DropDownSelectListWidget priorityWidget = (DropDownSelectListWidget) map3.get("Priority");
        list.add(priorityWidget.getFilterItemList());
        DropDownSelectListWidget delayWidget = (DropDownSelectListWidget) map3.get("Delay");
        list.add(delayWidget.getFilterItemList());
        DropDownSelectListWidget isAddedWidget = (DropDownSelectListWidget) map3.get("IsAdded");
        list.add(isAddedWidget.getFilterItemList());


        return list;

    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
        if (this.onOpenStateChangeAction != null) {
            this.onOpenStateChangeAction.run();
        }
    }

    public void setOnOpenStateChangeAction(Runnable action) {
        this.onOpenStateChangeAction = action;
    }

    public void switchButtonOnClick() {
        if (this.isOpen) {
            this.setOpen(false);
            this.enableSubWidgets(null);
        } else {
            this.setOpen(true);
            if (this.currentWidget == null) {
                this.currentWidget = this.timeButton;
            }
            this.enableSubWidgets(this.currentWidget);
        }

    }

    public void timeButtonOnClick() {
        this.selectTabButton(this.timeButton);
    }

    public void positionButtonOnClick() {
        this.selectTabButton(this.positionButton);
    }


    public void eventButtonOnClick() {
        this.selectTabButton(this.eventButton);
    }

    private void selectTabButton(TabButton eventButton) {
        this.setOpen(true);
        this.enableSubWidgets(eventButton);
        this.currentWidget = eventButton;
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
        for (HashMap<String, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
            for (ExpandableClickableWidget widget : widgetMap1.values()) {
                if (widget.isExpanded()) {
                    if (handler.apply(widget)) result = true;
                }
            }
        }
        if (!result) {
            for (HashMap<String, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
                for (ExpandableClickableWidget widget : widgetMap1.values()) {
                    if (!widget.isExpanded()) {
                        if (handler.apply(widget)) result = true;
                    }
                }
            }
            if (handler.apply(this.switchButton)) result = true;
            if (handler.apply(this.timeButton)) result = true;
            if (handler.apply(this.positionButton)) result = true;
            if (handler.apply(this.eventButton)) result = true;
        }
        return result;
    }


    public ArrayList<FilterType> getFilterItems() {
        // Time Button
        HashMap<String, ExpandableClickableWidget> map1 = this.widgetMap.get(this.timeButton);
        DropDownTextFieldListWidget gameTickWidget = (DropDownTextFieldListWidget) map1.get("GameTick");
        return gameTickWidget.getFilterItemList();

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
        for (HashMap<String, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
            for (ExpandableClickableWidget widget : widgetMap1.values()) {
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
