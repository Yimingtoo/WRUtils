package com.yiming.wrutils.client.gui.widget.filter;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.widget.filter.clickable.BaseClickableWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.*;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.ItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.item.SingleSelectItemListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.*;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.AreaListItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.BlockFilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.BlockItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.block.DimensionItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.bool_item.ScheduledTickAddedStatusItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.int_item.DelayItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.EventTypeItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.PriorityItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.SequenceItem;
import com.yiming.wrutils.client.gui.widget.filter.items.GameTickFilter;
import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.event.BaseEvent;
import com.yiming.wrutils.data.event.EventType;
import com.yiming.wrutils.data.event.MicroTimingSequence;
import com.yiming.wrutils.data.event.ScheduledTickInfo;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

public class FilterWidget extends ClickableWidget {
    private final HashMap<TabButton, HashMap<ItemType, ExpandableClickableWidget>> widgetMap = new HashMap<>();

    private boolean isOpen = false;
    private final BaseClickableWidget switchButton;
    private final TabButton timeButton;
    private final TabButton positionButton;
    private final TabButton eventButton;
    private int tabsWidth;

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

        x1 += this.switchButton.getWidth() + 5;
        this.timeButton = new TabButton(x + x1, y, 18, Text.of("Time"), this::timeButtonOnClick);
        x1 += this.timeButton.getWidth() + 5;
        this.positionButton = new TabButton(x + x1, y, 18, Text.of("Position"), this::positionButtonOnClick);
        x1 += this.positionButton.getWidth() + 5;
        this.eventButton = new TabButton(x + x1, y, 18, Text.of("Event"), this::eventButtonOnClick);
        this.tabsWidth = x1 + this.eventButton.getWidth();


        // 获得 DataManager.eventRecorder 中包含的方块Block
        HashSet<Block> targetBlockSet = new HashSet<>();
        HashSet<Block> sourceBlockSet = new HashSet<>();
        HashSet<Integer> delaySet = new HashSet<>();
        HashSet<MicroTimingSequence> sequenceSet = new HashSet<>();
        HashSet<TickPriority> tickPrioritySet = new HashSet<>();
        HashSet<EventType> eventTypeSet = new HashSet<>();
        for (BaseEvent event : DataManager.eventRecorder) {
            if (event.getTargetBlockInfo().state() != null) {
                targetBlockSet.add(event.getTargetBlockInfo().state().getBlock());
            }
            if (event.getSourceBlockInfo().state() != null) {
                sourceBlockSet.add(event.getSourceBlockInfo().state().getBlock());
            }
            if (event instanceof ScheduledTickInfo info) {
                delaySet.add(info.getDelay());
                tickPrioritySet.add(info.getPriority());
            }
            sequenceSet.add(event.getTimeStamp().sequence());
            eventTypeSet.add(event.getEventType());
        }

        this.widgetMap.put(this.timeButton, new HashMap<>());
//        ArrayList<GameTickItem> gameTickItems = new ArrayList<>();
        GameTickFilterDropDownWidget gameTickSelectWidget = new GameTickFilterDropDownWidget(10, y + 23, 100, 18, 200, 18, Text.of("Game Tick"), (GameTickFilter) FilterManager.GAME_TICK_FILTER);
        // 判断text是否为数字
        gameTickSelectWidget.getItemTextFieldListWidget().setHeaderTextFieldLostFocusFunction((text) -> text.trim().matches("\\d+"));
        // 判断是否为单个整数 || 判断是否为两个整数用 '-' 分割
        gameTickSelectWidget.getItemTextFieldListWidget().setItemTextFieldLostFocusFunction((text) -> text.trim().matches("\\d+") || text.trim().matches("\\d+\\s*-\\s*\\d+"));
        //  设置header的text
//        if (!DataManager.eventRecorder.isEmpty()) {
//            long time = DataManager.eventRecorder.getFirst().getTimeStamp().gameTime();
//            gameTickSelectWidget.getItemTextFieldListWidget().getHeaderEntry().setTextFieldText(String.valueOf(time));
//        }
        this.widgetMap.get(this.timeButton).put(ItemType.GAME_TICK, gameTickSelectWidget);
        this.widgetMap.get(this.timeButton).put(ItemType.SEQUENCE, new DropDownSelectListWidget(115, y + 23, 100, 18, 100, 18, Text.of("Sequence"), FilterManager.SEQUENCE_FILTER.updateFilter(sequenceSet)));

        this.widgetMap.put(this.positionButton, new HashMap<>());

//        DropDownSingleSelectListWidget targetAreaWidget = new DropDownSingleSelectListWidget(10, y + 23, 90, 18, 200, 18, Text.of("Target Area"), AreaListItem.getAreaListItems(DataManager.areaGroupManagement.getList(), BlockFilterType.TARGET));
//        SingleSelectItemListWidget w1 = (SingleSelectItemListWidget) targetAreaWidget.getItemListWidget();
//        w1.setHeaderItemEntry(new ItemListWidget.HeaderItemEntry());
//        w1.setSingleCheckedItem((ItemListWidget.ItemEntry) w1.getFirst());
//        this.widgetMap.get(this.positionButton).put(ItemType.SOURCE_AREA_LIST, targetAreaWidget);
//        this.widgetMap.get(this.positionButton).put(ItemType.SOURCE_BLOCK, new DropDownSelectListWidget(105, y + 23, 90, 18, 100, 18, Text.of("Target Block"), BlockItem.getBlockItems(targetBlockSet, BlockFilterType.TARGET)));
//        DropDownSingleSelectListWidget sourceAreaWidget = new DropDownSingleSelectListWidget(210, y + 23, 90, 18, 200, 18, Text.of("Source Area"), AreaListItem.getAreaListItems(DataManager.areaGroupManagement.getList(), BlockFilterType.SOURCE));
//        SingleSelectItemListWidget w2 = (SingleSelectItemListWidget) sourceAreaWidget.getItemListWidget();
//        w2.setHeaderItemEntry(new ItemListWidget.HeaderItemEntry());
//        w2.setSingleCheckedItem((ItemListWidget.ItemEntry) w2.getFirst());
//        this.widgetMap.get(this.positionButton).put(ItemType.TARGET_AREA_LIST, sourceAreaWidget);
//        this.widgetMap.get(this.positionButton).put(ItemType.TARGET_BLOCK, new DropDownSelectListWidget(305, y + 23, 90, 18, 100, 18, Text.of("Source Block"), BlockItem.getBlockItems(sourceBlockSet, BlockFilterType.SOURCE)));
//        this.widgetMap.get(this.positionButton).put(ItemType.DIMENSION, new DropDownSelectListWidget(405, y + 23, 90, 18, 100, 18, Text.of("Dimension"), DimensionItem.getDimensionItems(BlockFilterType.TARGET)));

        this.widgetMap.put(this.eventButton, new HashMap<>());
        this.widgetMap.get(this.eventButton).put(ItemType.EVENT_TYPE, new DropDownSelectListWidget(10, y + 23, 95, 18, 200, 18, Text.of("Event Type"), FilterManager.EVENT_TYPE_FILTER.updateFilter(eventTypeSet)));
        this.widgetMap.get(this.eventButton).put(ItemType.DELAY, new DropDownSelectListWidget(110, y + 23, 95, 18, 200, 18, Text.of("Delay"), FilterManager.DELAY_FILTER.updateFilter(delaySet)));
        this.widgetMap.get(this.eventButton).put(ItemType.PRIORITY, new DropDownSelectListWidget(210, y + 23, 95, 18, 200, 18, Text.of("Priority"), FilterManager.PRIORITY_FILTER.updateFilter(tickPrioritySet)));
        this.widgetMap.get(this.eventButton).put(ItemType.SCHEDULED_TICK_ADDED_STATUS, new DropDownSelectListWidget(310, y + 23, 95, 18, 200, 18, Text.of("Is Added"), FilterManager.SCHEDULED_TICK_ADD_STATUS_FILTER));

        this.enableSubWidgets(null);

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

    public int getTabsWidth() {
        return this.tabsWidth;
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
        for (HashMap<ItemType, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
            for (ExpandableClickableWidget widget : widgetMap1.values()) {
                if (widget.isExpanded()) {
                    if (handler.apply(widget)) result = true;
                }
            }
        }
        if (!result) {
            for (HashMap<ItemType, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
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

    public void resetFilter() {
        for (HashMap<ItemType, ExpandableClickableWidget> widgetMap : this.widgetMap.values()) {
            for (ExpandableClickableWidget widget : widgetMap.values()) {
                widget.reset();
            }
        }
        DataManagerClient.resetFilter();
    }

    public void updateFilterTypeList() {

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
        for (HashMap<ItemType, ExpandableClickableWidget> widgetMap1 : this.widgetMap.values()) {
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

        public TabButton(int x, int y, int height, Text message, Runnable onClickAction) {
            super(x, y, 0, height, message, onClickAction);
            this.width = this.textRenderer.getWidth(this.getMessage()) + 5 * 2;
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
