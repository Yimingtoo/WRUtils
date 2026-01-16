package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.clickable.AddRemoveButtonWidget;
import com.yiming.wrutils.client.gui.widget.filter.clickable.TextButtonWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.GameTickFilterDropDownWidget;

import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;
import com.yiming.wrutils.client.gui.widget.filter.items.GameTickFilter;
import com.yiming.wrutils.data.DataManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GameTickItemsWidget extends AlwaysSelectedEntryListWidget<GameTickItemsWidget.Entry> {
    private final ArrayList<ItemEntry> itemEntries = new ArrayList<>();
    private final ItemHeaderEntry headerEntry = new ItemHeaderEntry(new GameTickFilter.OriginalTickItem(0), this.headerTextFieldLostFocusFunction);
    private final GameTickFilterDropDownWidget parent;
    private Function<String, Boolean> itemTextFieldLostFocusFunction = null;
    private Function<String, Boolean> headerTextFieldLostFocusFunction = null;
    private final GameTickFilter filter;


    public GameTickItemsWidget(MinecraftClient minecraftClient, int width, int x, int y, int itemHeight, GameTickFilterDropDownWidget parent, GameTickFilter filter) {
        super(minecraftClient, width, itemHeight * 2 + 6, y, itemHeight);
        this.parent = parent;
        this.filter = filter;
        this.setX(x);
    }

    public void updateEntries() {
        this.clearEntries();
        this.addEntry(this.headerEntry);
        this.itemEntries.forEach(this::addEntry);
        this.refreshScroll();
        this.setHeight(this.itemHeight * Math.min(this.itemEntries.size() + 1, 6) + 10);
    }


    public void setItemEntries(GameTickFilter filter) {
        this.itemEntries.clear();
        this.headerEntry.setItem(filter.getOriginalTickItem());
        DataManagerClient.eventOriginTick = DataManager.getEventRecordFirstTick();
        this.headerEntry.setTextFieldTextAndItem(DataManagerClient.eventOriginTick);

        for (FilterItem item : filter.getItems()) {
            if (item instanceof GameTickFilter.Item item1) {
                ItemEntry itemEntry = new ItemEntry(item1, this.itemTextFieldLostFocusFunction);
                itemEntry.setOnRemoveAction(() -> this.removeItemTextFieldEntry(itemEntry));
                this.itemEntries.add(itemEntry);
            }
        }
        this.updateEntries();
    }


    public void addItemEntry(GameTickFilter.Item item) {
        this.filter.addItem(item);
        ItemEntry itemEntry = new ItemEntry(item, this.itemTextFieldLostFocusFunction);
        itemEntry.setOnRemoveAction(() -> {
            this.filter.removeItem(item);
            this.removeItemTextFieldEntry(itemEntry);
        });
        this.itemEntries.add(itemEntry);
        this.updateEntries();
        this.setSelected(itemEntry);
        this.setScrollY(this.getMaxScrollY());
    }

    public void removeItemTextFieldEntry(ItemEntry itemEntry) {
        this.itemEntries.remove(itemEntry);
        this.updateEntries();
    }

    public void setItemTextFieldLostFocusFunction(Function<String, Boolean> function) {
        this.itemTextFieldLostFocusFunction = function;
        for (ItemEntry itemEntry : this.itemEntries) {
            itemEntry.setTextFieldLostFocusFunction(function);
        }
    }

    public void setHeaderTextFieldLostFocusFunction(Function<String, Boolean> function) {
        this.headerTextFieldLostFocusFunction = function;
        this.headerEntry.setTextFieldLostFocusFunction(function);
    }

    public List<String> getCheckedItems() {
        return this.itemEntries.stream()
                .filter(entry -> entry.isChecked() != CheckState.UNCHECKED)
                .map(Entry::getItemName).toList();
    }

    public CheckState getPatrentCheckState() {
        CheckState checkState = CheckState.CHECKED;
        int size = this.getCheckedItems().size();
        if (size == 0) {
            if (!this.itemEntries.isEmpty()) {
                checkState = CheckState.UNCHECKED;
            }
        } else if (size < this.itemEntries.size()) {
            checkState = CheckState.INDETERMINATE;
        }
        return checkState;
    }

    public void setCheckedItems(CheckState checkState) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry itemEntry) {
                itemEntry.setChecked(checkState);
            }
        });
    }


    public boolean isTextFieldFocused() {
        return this.children().stream().anyMatch(entry -> entry.getTextField().isFocused());
    }

    public void removeTextFieldFocused() {
        this.children().forEach(entry -> entry.getTextField().setFocused(false));
    }

    public void reset() {
        this.itemEntries.clear();
        this.filter.clear();
        this.updateEntries();
        this.headerEntry.updateTextFieldFromItem();
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawMenuListBackground(DrawContext context) {
        context.fill(this.getX(), this.getY() - 1, this.getX() + this.width, this.height + this.getY() + 1, 0xFFFFFFFF);
        context.fill(this.getX() + 1, this.getY(), this.getX() + this.width - 1, this.getY() + this.height, 0xFF101010);
    }

    @Override
    protected void drawHeaderAndFooterSeparators(DrawContext context) {
    }

    @Override
    protected int getScrollbarX() {
        return this.getX() + this.getWidth() - 8;
    }

    @Override
    public int getRowWidth() {
        return Math.max(this.width - 12, 10);
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        int i = this.getX() + (this.width - entryWidth) / 2 - 3;
        int j = this.getX() + (this.width + entryWidth) / 2 - 3;
        context.fill(i, y, j, y + entryHeight + 4, 0xFF202020);
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        super.setFocused(focused);
        this.parent.setCheckState(this.getPatrentCheckState());
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = this.isMouseOver(mouseX, mouseY);
        for (Entry entry : this.children()) {
            if (!entry.getTextField().isMouseOver(mouseX, mouseY)) {
                entry.getTextField().setFocused(false);
            }
        }
        boolean bl1 = this.checkScrollbarDragged(mouseX, mouseY, button);
        return bl1 || bl && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.isMouseOver(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return false;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {

        return super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected int getContentsHeightWithPadding() {
        return this.getEntryCount() * this.itemHeight + this.headerHeight + 8;
    }


    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        protected final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        protected FilterItem item;
        protected final CustomTextFieldWidget textField;

        public Entry(FilterItem item) {
            this.item = item;
            this.textField = new CustomTextFieldWidget(this.textRenderer, 120, 14, Text.of(""));
            this.updateTextFieldFromItem();
            this.textField.setEditable(true);
            this.textField.setMaxLength(20);
        }

        public String getItemName() {
            return item.getName();
        }

        public void setItem(FilterItem item) {
            this.item = item;
        }

        public CheckState isChecked() {
            return item.isChecked();
        }

        public TextFieldWidget getTextField() {
            return textField;
        }

        public void updateTextFieldFromItem() {
            this.textField.setText(this.getItemName());
        }

        public void setTextFieldTextAndItem(String text) {
            this.textField.setText(text);
            this.item.setValueFromText(text);
        }

        public void setTextFieldLostFocusFunction(Function<String, Boolean> textFieldLostFocusFunction) {
            Function<String, Boolean> function = (String text) -> {
                this.item.setValueFromText(text);
                return textFieldLostFocusFunction.apply(text);
            };
            this.textField.setLostFocusFunction(function);
        }

        public boolean onClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.textField.mouseClicked(mouseX, mouseY, button)) {
                this.textField.setFocused(true);
                return true;
            }
            if (this.onClicked(mouseX, mouseY, button)) {
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean charTyped(char chr, int keyCode) {
            return this.textField.charTyped(chr, keyCode);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return this.textField.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public Text getNarration() {
            return Text.of("Entry");
        }
    }

    public static class ItemEntry extends GameTickItemsWidget.Entry {
        protected final AddRemoveButtonWidget removeButton;
        protected Runnable onRemoveAction = null;

        public ItemEntry(GameTickFilter.Item item, Function<String, Boolean> textFieldLostFocusFunction) {
            super(item);
            this.removeButton = new AddRemoveButtonWidget(0, 0, 14, 14, false, () -> {
                System.out.println("remove click");
                if (this.onRemoveAction != null) {
                    this.onRemoveAction.run();
                }
            });
            this.setTextFieldLostFocusFunction(textFieldLostFocusFunction);
        }

        public void setOnRemoveAction(Runnable onRemoveAction) {
            this.onRemoveAction = onRemoveAction;
        }

        public GameTickFilter.Item getItem() {
            return (GameTickFilter.Item) this.item;
        }

        public void setChecked(CheckState checked) {
            if (!(this.textField.isFocused() || this.removeButton.isFocused())) {
                this.item.setChecked(checked);
            }
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int yCenter = y + entryHeight / 2 + 2;
            this.textField.setPosition(x + 15, yCenter - this.textField.getHeight() / 2);
            this.textField.render(context, mouseX, mouseY, tickDelta);
            this.removeButton.setPosition(x + 150, yCenter - this.removeButton.getHeight() / 2);
            this.removeButton.render(context, mouseX, mouseY, tickDelta);

            context.fill(x - 1, yCenter - 4, x + 8, yCenter + 5, Colors.WHITE);
            context.fill(x, yCenter - 3, x + 7, yCenter + 4, Colors.BLACK);

            if (this.isChecked() != CheckState.UNCHECKED) {
                context.fill(x + 1, yCenter - 2, x + 6, yCenter + 3, Colors.GREEN);
            }
        }

        @Override
        public boolean onClicked(double mouseX, double mouseY, int button) {
            if (this.removeButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            if (this.isChecked() == CheckState.UNCHECKED) {
                this.item.setChecked(CheckState.CHECKED);
            } else {
                this.item.setChecked(CheckState.UNCHECKED);
            }
            return super.onClicked(mouseX, mouseY, button);
        }


    }

    public static class ItemHeaderEntry extends Entry {
        private final TextButtonWidget textButtonWidget;
        private final String titleText = "Origin Tick:";
        int textWidth;

        public ItemHeaderEntry(GameTickFilter.OriginalTickItem item, Function<String, Boolean> textFieldLostFocusFunction) {
            super(item);
            this.textField.setLostFocusFunction(textFieldLostFocusFunction);
            this.textWidth = this.textRenderer.getWidth(this.titleText);
            this.textButtonWidget = new TextButtonWidget(0, 0, 35, 14, Text.of("Reset"), () -> this.setTextFieldTextAndItem(DataManager.getEventRecordFirstTick()));
            this.textField.setWidth(142 - this.textWidth - 6);
        }


        public GameTickFilter.OriginalTickItem getItem() {
            return (GameTickFilter.OriginalTickItem) this.item;
        }

        public void setTextFieldTextAndItem(long tick) {
            this.textField.setText(String.valueOf(tick));
            this.getItem().setValue(tick);
        }


        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int yCenter = y + entryHeight / 2 + 2;
            context.drawTextWithShadow(this.textRenderer, this.titleText, x, yCenter - 4, Colors.WHITE);
            this.textField.setPosition(x + this.textWidth + 6, yCenter - this.textField.getHeight() / 2);
            this.textField.render(context, mouseX, mouseY, tickDelta);

            this.textButtonWidget.setPosition(x + 145, yCenter - this.textButtonWidget.getHeight() / 2);
            this.textButtonWidget.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean onClicked(double mouseX, double mouseY, int button) {
            if (this.textButtonWidget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return super.onClicked(mouseX, mouseY, button);
        }
    }
}
