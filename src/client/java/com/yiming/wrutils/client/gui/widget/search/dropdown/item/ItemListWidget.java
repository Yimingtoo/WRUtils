package com.yiming.wrutils.client.gui.widget.search.dropdown.item;

import com.yiming.wrutils.client.gui.widget.search.dropdown.CheckState;
import com.yiming.wrutils.client.gui.widget.search.dropdown.DropDownSelectListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemListWidget extends AlwaysSelectedEntryListWidget<ItemListWidget.Entry> {
    private final ArrayList<ItemEntry> itemEntries = new ArrayList<>();
    private final DropDownSelectListWidget parent;
    private boolean isSingleChecked;

    public ItemListWidget(MinecraftClient minecraftClient, int width, int height, int x, int y, int itemHeight, DropDownSelectListWidget parent) {
        this(minecraftClient, width, height, x, y, itemHeight, parent, false);
    }

    public ItemListWidget(MinecraftClient minecraftClient, int width, int height, int x, int y, int itemHeight, DropDownSelectListWidget parent, boolean isSingleChecked) {
        super(minecraftClient, width, height, y, itemHeight);
        this.parent = parent;
        this.setX(x);
        this.isSingleChecked = isSingleChecked;
    }

    public void updateEntries() {
        this.clearEntries();
        this.itemEntries.forEach(this::addEntry);
        this.refreshScroll();
    }

    public void setItemEntries(ArrayList<String> items) {
        items.forEach(item -> this.itemEntries.add(new ItemEntry(item)));
        this.updateEntries();
    }

    public List<String> getCheckedItems() {
        return this.children().stream()
                .filter(entry -> {
                    if (entry instanceof ItemEntry itemEntry) {
                        return itemEntry.isChecked;
                    }
                    return false;
                })
                .map(entry -> ((ItemEntry) entry).itemName).toList();
    }

    public void setCheckedItems(boolean checked) {
        if (!this.isSingleChecked) {
            this.children().forEach(entry -> {
                if (entry instanceof ItemEntry itemEntry) {
                    itemEntry.setChecked(checked);
                }
            });
        } else {
            if (!checked) {
                this.children().forEach(entry -> {
                    if (entry instanceof ItemEntry itemEntry) {
                        itemEntry.setChecked(false);
                    }
                });
            } else {

            }
        }
    }

    public void setSingleCheckedItem(ItemEntry itemEntry) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry itemEntry1) {
                itemEntry1.setChecked(itemEntry1 == itemEntry && !itemEntry1.isChecked());
            }
        });
    }

    public void setSingleChecked(boolean isSingleChecked) {
        this.isSingleChecked = isSingleChecked;
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry itemEntry1) {
                itemEntry1.setIsSingleChecked(isSingleChecked);
            }
        });
    }

    public int getItemCount() {
        return this.children().size();
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
        if (this.getSelectedOrNull() instanceof ItemEntry entry) {
            if (this.isSingleChecked) {
                this.setSingleCheckedItem(entry);
                if(this.getCheckedItems().isEmpty()){
                    this.parent.setCheckState(CheckState.UNCHECKED);
                }else {
                    this.parent.setCheckState(CheckState.CHECKED);
                }
            } else {
                entry.setChecked(!entry.isChecked());
                int size = this.getCheckedItems().size();
                if (size == 0) {
                    this.parent.setCheckState(CheckState.UNCHECKED);
                } else if (size < this.children().size()) {
                    this.parent.setCheckState(CheckState.INDETERMINATE);
                } else {
                    this.parent.setCheckState(CheckState.CHECKED);
                }
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
        private final String itemName;
        private boolean isChecked;
        private boolean isSingleChecked;

        public ItemEntry(String itemName) {
            this.itemName = itemName;
            this.isChecked = true;
            this.isSingleChecked = false;
        }

        public ItemEntry(String itemName, boolean isSingleChecked) {
            this.itemName = itemName;
            this.isChecked = true;
            this.isSingleChecked = isSingleChecked;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            this.isChecked = checked;
        }

        public void setIsSingleChecked(boolean checked) {
            this.isSingleChecked = checked;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

            context.drawTextWithShadow(this.textRenderer, itemName, x + 15, y + entryHeight / 2 - 2, Colors.WHITE);
            if (!this.isSingleChecked) {
                context.fill(x - 1, y + entryHeight / 2 - 4, x + 8, y + entryHeight / 2 + 5, Colors.WHITE);
                context.fill(x, y + entryHeight / 2 - 3, x + 7, y + entryHeight / 2 + 4, Colors.BLACK);
            }
            if (this.isChecked) {
                context.fill(x + 1, y + entryHeight / 2 - 2, x + 6, y + entryHeight / 2 + 3, Colors.GREEN);
            }
        }
    }
}
