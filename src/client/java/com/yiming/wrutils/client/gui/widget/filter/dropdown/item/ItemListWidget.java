package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterTypeTemp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ItemListWidget extends AlwaysSelectedEntryListWidget<ItemListWidget.Entry> {
    protected final ArrayList<ItemEntry> itemEntries = new ArrayList<>();
    protected String masterString = null;
    private HeaderItemEntry headerItemEntry = null;
    protected Runnable onFocusedAction;

    public ItemListWidget(MinecraftClient minecraftClient, int width, int x, int y, int itemHeight) {
        super(minecraftClient, width, 0, y, itemHeight);
        this.setX(x);
    }

    public void updateEntries() {
        this.clearEntries();
        this.itemEntries.forEach(this::addEntry);
        this.refreshScroll();
        this.setHeight(this.itemHeight * Math.min(this.itemEntries.size(), 6) + 10);
    }

    public void setItemEntries(FilterTypeTemp filter) {
        this.itemEntries.clear();
        if (this.headerItemEntry != null) {
            this.itemEntries.add(this.headerItemEntry);
        }
        for (FilterItem item : filter.getItems()) {
            this.itemEntries.add(new ItemEntry(item));
        }
        this.updateEntries();
    }

    public ArrayList<ItemEntry> getItemEntries() {
        return this.itemEntries;
    }

    public ArrayList<ItemEntry> getCheckedItemEntries() {
        return this.getItemEntries().stream()
                .filter(entry -> entry.getCheckState() == CheckState.CHECKED)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getCheckedCount() {
        return (int) this.children().stream()
                .filter(entry -> {
                    if (entry instanceof ItemEntry itemEntry) {
                        return itemEntry.getCheckState() != CheckState.UNCHECKED;
                    }
                    return false;
                })
                .count();
    }

    public CheckState getPatrentCheckState() {
        CheckState checkState = CheckState.CHECKED;
        int size = this.getCheckedCount();
        if (size == 0) {
            if (!this.itemEntries.isEmpty()) {
                checkState = CheckState.UNCHECKED;
            }
        } else if (size < this.itemEntries.size()) {
            checkState = CheckState.INDETERMINATE;
        }
        return checkState;
    }

    public ItemEntry getFirstCheckedOrNot() {
        return (ItemEntry) this.children().stream()
                .filter(entry -> {
                    if (entry instanceof ItemEntry itemEntry) {
                        return itemEntry.getCheckState() != CheckState.UNCHECKED;
                    }
                    return false;
                })
                .findFirst().orElse(null);
    }


    public void setSelectedCheckedItems(boolean checked) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry itemEntry) {
                itemEntry.setCheckState(checked ? CheckState.CHECKED : CheckState.UNCHECKED);
            }
        });
    }


    public void setOnFocusedAction(Runnable onFocusedAction) {
        this.onFocusedAction = onFocusedAction;
    }


    public void setMasterString(String masterString) {
        this.masterString = masterString;
    }

    public String getMasterString() {
        return this.masterString;
    }

    public void setHeaderItemEntry(HeaderItemEntry headerItemEntry) {
        this.headerItemEntry = headerItemEntry;
        ArrayList<ItemEntry> itemEntriesClone = new ArrayList<>(this.itemEntries);
        this.itemEntries.clear();
        this.itemEntries.add(this.headerItemEntry);
        this.itemEntries.addAll(itemEntriesClone);
        this.updateEntries();
    }

    public void reset() {
        this.itemEntries.forEach(entry -> entry.setCheckState(CheckState.CHECKED));
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
        int i = this.getX() + (this.width - entryWidth) / 2;
        int j = this.getX() + (this.width + entryWidth) / 2;
        context.fill(i, y, j, y + entryHeight + 4, 0xFF202020);
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        super.setFocused(focused);
        this.setOnFocused();
    }

    protected void setOnFocused() {
        if (this.getSelectedOrNull() instanceof ItemEntry entry) {
            if (entry.getCheckState() == CheckState.CHECKED) {
                entry.setCheckState(CheckState.UNCHECKED);
            } else {
                entry.setCheckState(CheckState.CHECKED);
            }
            if (this.onFocusedAction != null) {
                this.onFocusedAction.run();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = this.isMouseOver(mouseX, mouseY);
        return this.isMouseOver(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.isMouseOver(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        return false;
    }


    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        protected final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        @Override
        public Text getNarration() {
            return Text.of("Entry");
        }
    }

    public static class ItemEntry extends ItemListWidget.Entry {
        protected final FilterItem item;

        public ItemEntry(FilterItem item) {
            this.item = item;
        }

        public void setCheckState(CheckState checkState) {
            this.item.setChecked(checkState);
        }

        public CheckState getCheckState() {
            return this.item.isChecked();
        }

        public String getItemName() {
            return this.item.getName();
        }

        public FilterItem getItem() {
            return this.item;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(this.textRenderer, this.getItemName(), x + 15, y + entryHeight / 2 - 2, Colors.WHITE);
            context.fill(x - 1, y + entryHeight / 2 - 4, x + 8, y + entryHeight / 2 + 5, Colors.WHITE);
            context.fill(x, y + entryHeight / 2 - 3, x + 7, y + entryHeight / 2 + 4, Colors.BLACK);
            if (this.item.isChecked() == CheckState.CHECKED) {
                context.fill(x + 1, y + entryHeight / 2 - 2, x + 6, y + entryHeight / 2 + 3, Colors.GREEN);
            } else if (this.item.isChecked() == CheckState.INDETERMINATE) {
                context.fill(x + 1, y + entryHeight / 2, x + 6, y + entryHeight / 2 + 1, Colors.GREEN);
            }
        }
    }

    public static class HeaderItemEntry extends ItemEntry {
        public HeaderItemEntry() {
            super(new FilterItem() {
                @Override
                public String getName() {
                    return "Any";
                }
            });
        }
    }
}
