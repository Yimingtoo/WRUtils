package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.gui.widget.CustomTextFieldWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.clickable.AddRemoveButtonWidget;
import com.yiming.wrutils.client.gui.widget.filter.clickable.TextButtonWidget;
import com.yiming.wrutils.client.gui.widget.filter.dropdown.DropDownTextFieldListWidget;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import com.yiming.wrutils.client.gui.widget.filter.item.items.long_item.LongItem;
import com.yiming.wrutils.client.gui.widget.filter.item.items.long_item.OriginTickItem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemTextFieldListWidget extends AlwaysSelectedEntryListWidget<ItemTextFieldListWidget.Entry> {
    private static final Logger log = LoggerFactory.getLogger(ItemTextFieldListWidget.class);
    private final ArrayList<ItemTextFieldEntry> itemEntries = new ArrayList<>();
    private final ItemHeaderTextFieldEntry headerEntry = new ItemHeaderTextFieldEntry(new OriginTickItem(0), this.headerTextFieldLostFocusFunction);
    private DropDownTextFieldListWidget parent;
    private Function<String, Boolean> itemTextFieldLostFocusFunction = null;
    private Function<String, Boolean> headerTextFieldLostFocusFunction = null;


    public ItemTextFieldListWidget(MinecraftClient minecraftClient, int width, int x, int y, int itemHeight, DropDownTextFieldListWidget parent) {
        super(minecraftClient, width, itemHeight * 2 + 6, y, itemHeight);
        this.parent = parent;
        this.setX(x);
        this.itemEntries.add(this.headerEntry);
    }

    public void updateEntries() {
        this.clearEntries();
        this.itemEntries.forEach(this::addEntry);
        this.refreshScroll();
        this.setHeight(this.itemHeight * Math.min(this.itemEntries.size(), 6) + 10);
    }

    public void setItemEntries(ArrayList<? extends FilterType<?>> items) {
        items.forEach(item -> {
            ItemTextFieldEntry itemTextFieldEntry = new ItemTextFieldEntry(item, this.itemTextFieldLostFocusFunction);
            itemTextFieldEntry.setOnRemoveAction(() -> this.removeItemTextFieldEntry(itemTextFieldEntry));
            this.itemEntries.add(itemTextFieldEntry);
        });
        this.updateEntries();
    }


    public void addItemEntry(FilterType<?> item) {
        ItemTextFieldEntry itemTextFieldEntry = new ItemTextFieldEntry(item, this.itemTextFieldLostFocusFunction);
        itemTextFieldEntry.setOnRemoveAction(() -> this.removeItemTextFieldEntry(itemTextFieldEntry));
        this.itemEntries.add(itemTextFieldEntry);
        this.updateEntries();
        this.setSelected(itemTextFieldEntry);
        this.setScrollY(this.getMaxScrollY());
    }

    public void removeItemTextFieldEntry(ItemTextFieldEntry itemTextFieldEntry) {
        this.itemEntries.remove(itemTextFieldEntry);
        this.updateEntries();
    }

    public void setHeaderEntryText(String text) {
        this.headerEntry.getTextField().setText(text);
    }

    public void setItemTextFieldLostFocusFunction(Function<String, Boolean> function) {
        this.itemTextFieldLostFocusFunction = function;
        for (ItemTextFieldEntry itemTextFieldEntry : this.itemEntries) {
            if (!(itemTextFieldEntry instanceof ItemHeaderTextFieldEntry)) {
                itemTextFieldEntry.setTextFieldLostFocusFunction(function);
            }
        }
    }

    public void setHeaderTextFieldLostFocusFunction(Function<String, Boolean> function) {
        this.headerTextFieldLostFocusFunction = function;
        this.headerEntry.setTextFieldLostFocusFunction(function);
    }

    public ItemHeaderTextFieldEntry getHeaderEntry() {
        return this.headerEntry;
    }


    public List<String> getCheckedItems() {
        return this.children().stream()
                .filter(entry -> {
                    if (entry instanceof ItemTextFieldEntry itemTextFieldEntry) {
                        if (!(entry instanceof ItemHeaderTextFieldEntry)) {
                            return itemTextFieldEntry.isChecked;
                        }
                    }
                    return false;
                })
                .map(entry -> ((ItemTextFieldEntry) entry).getItemName()).toList();
    }

    public void updateParentWidgetCheckedState() {
        int size = this.getCheckedItems().size();
        if (size == 0) {
            this.parent.setCheckState(CheckState.UNCHECKED);
        } else if (size < this.children().size() - 1) {
            this.parent.setCheckState(CheckState.INDETERMINATE);
        } else {
            this.parent.setCheckState(CheckState.CHECKED);
        }
    }

    public void setCheckedItems(boolean checked) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemTextFieldEntry itemTextFieldEntry) {
                itemTextFieldEntry.setChecked(checked);
            }
        });
    }

    public int getItemCount() {
        return this.children().size();
    }

    public boolean isTextFieldFocused() {
        return this.children().stream().anyMatch(entry -> entry instanceof ItemTextFieldEntry itemTextFieldEntry && itemTextFieldEntry.getTextField().isFocused());
    }

    public void removeTextFieldFocused() {
        this.children().forEach(entry -> {
            if (entry instanceof ItemTextFieldEntry itemTextFieldEntry) {
                itemTextFieldEntry.getTextField().setFocused(false);
            }
        });
    }

    public void reset() {
        this.itemEntries.clear();
        this.itemEntries.add(this.headerEntry);
        this.updateEntries();
        if (!DataManager.eventRecorder.isEmpty()) {
            long time = DataManager.eventRecorder.getFirst().getTimeStamp().gameTime();
            ((LongItem) this.headerEntry.getItem()).setValue(time);
            this.headerEntry.setTextFieldText(String.valueOf(time));

        } else {
            ((LongItem) this.headerEntry.getItem()).setValue(0L);
            this.headerEntry.setTextFieldText(String.valueOf(0L));
        }
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

        this.updateParentWidgetCheckedState();
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = this.isMouseOver(mouseX, mouseY);
        for (Entry entry : this.children()) {
            if (entry instanceof ItemTextFieldEntry itemTextFieldEntry) {
                if (!itemTextFieldEntry.getTextField().isMouseOver(mouseX, mouseY)) {
                    itemTextFieldEntry.getTextField().setFocused(false);
                }
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

        @Override
        public Text getNarration() {
            return Text.of("Entry");
        }
    }

    public static class ItemTextFieldEntry extends ItemTextFieldListWidget.Entry {
        protected final FilterType<?> item;

        protected boolean isChecked;
        protected final CustomTextFieldWidget textField;
        protected final AddRemoveButtonWidget removeButton;

        protected Runnable onRemoveAction = null;

        public ItemTextFieldEntry(FilterType<?> item, Function<String, Boolean> textFieldLostFocusFunction) {
            this(item);
            this.setTextFieldLostFocusFunction(textFieldLostFocusFunction);
        }

        public ItemTextFieldEntry(FilterType<?> item) {
            this.item = item;
            this.isChecked = true;
            this.textField = new CustomTextFieldWidget(this.textRenderer, 120, 14, Text.of(this.getItemName()));
            this.textField.setText(this.getItemName());
            this.textField.setEditable(true);
            this.textField.setMaxLength(20);
            this.removeButton = new AddRemoveButtonWidget(0, 0, 14, 14, false, () -> {
                System.out.println("remove click");
                if (this.onRemoveAction != null) {
                    this.onRemoveAction.run();
                }
            });
        }

        public void setTextFieldLostFocusFunction(Function<String, Boolean> textFieldLostFocusFunction) {
            Function<String, Boolean> function = (String text) -> {
                this.item.setValueFromText(text);
                return textFieldLostFocusFunction.apply(text);
            };
            this.textField.setLostFocusFunction(function);
        }

        public void setOnRemoveAction(Runnable onRemoveAction) {
            this.onRemoveAction = onRemoveAction;
        }

        public TextFieldWidget getTextField() {
            return textField;
        }

        public FilterType<?> getItem() {
            return item;
        }

        public void updateTextField() {
            this.textField.setText(this.getItemName());
        }

        public String getItemName() {
            return item.getName();
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            if (!(this.textField.isFocused() || this.removeButton.isFocused())) {
                this.isChecked = checked;
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

            if (this.isChecked) {
                context.fill(x + 1, yCenter - 2, x + 6, yCenter + 3, Colors.GREEN);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.textField.mouseClicked(mouseX, mouseY, button)) {
                this.textField.setFocused(true);
                return true;
            }
            if (this.removeButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            this.isChecked = !this.isChecked;
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
    }

    public static class ItemHeaderTextFieldEntry extends ItemTextFieldEntry {
        TextButtonWidget textButtonWidget;
        private final String titleText = "Origin Tick:";
        int textWidth;

        public ItemHeaderTextFieldEntry(FilterType<?> item, Function<String, Boolean> textFieldLostFocusFunction) {
            this(item);
            this.textField.setLostFocusFunction(textFieldLostFocusFunction);
        }

        public ItemHeaderTextFieldEntry(FilterType<?> item) {
            super(item);
            this.removeButton.active = false;
            this.removeButton.visible = false;
            this.textWidth = this.textRenderer.getWidth(this.titleText);

            this.textButtonWidget = new TextButtonWidget(0, 0, 35, 14, Text.of("Reset"), () -> {
                String text = String.valueOf(DataManager.eventRecorder.getFirst().getTimeStamp().gameTime());
                this.textField.setText(text);
                this.item.setValueFromText(text);
            });
            this.textField.setWidth(142 - this.textWidth - 6);
        }

        public void setTextFieldText(String text) {
            this.textField.setText(text);
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
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.textButtonWidget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

    }

}
