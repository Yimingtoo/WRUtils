package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.items.FilterItem;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;
import java.util.function.BiConsumer;

public class SingleSelectItemListWidget extends ItemListWidget {
    private ItemEntry selectedItemEntry = null;
    private BiConsumer<ItemEntry, Boolean> onEntryClicked = null;

    public SingleSelectItemListWidget(MinecraftClient minecraftClient, int width, int x, int y, int itemHeight) {
        super(minecraftClient, width, x, y, itemHeight);
    }

    public void setSingleCheckedItem(ItemEntry itemEntry) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry itemEntry1) {
                itemEntry1.setCheckState(itemEntry1 == itemEntry ? CheckState.CHECKED : CheckState.UNCHECKED);
            }
        });
    }

    public ItemEntry getSelectedEntry() {
        return this.selectedItemEntry;
    }

    public void setOnEntryClicked(BiConsumer<ItemEntry, Boolean> onEntryClicked) {
        this.onEntryClicked = onEntryClicked;
    }

    @Override
    public void reset() {
        if (this.getFirst() instanceof ItemEntry entry) {
            this.setSingleCheckedItem(entry);
            this.selectedItemEntry = entry;
        }
    }

    @Override
    public void setSelectedCheckedItems(boolean checked) {
        if (!checked) {
            this.children().forEach(entry -> {
                if (entry instanceof ItemEntry itemEntry) {
                    itemEntry.setCheckState(CheckState.UNCHECKED);
                }
            });
        } else {
            if (this.selectedItemEntry != null) {
                this.selectedItemEntry.setCheckState(CheckState.CHECKED);
            } else if (!this.children().isEmpty() && this.getFirst() instanceof ItemEntry entry) {
                this.selectedItemEntry = entry;
                entry.setCheckState(CheckState.CHECKED);
            }
        }
    }

    @Override
    protected void setOnFocused() {
        if (this.getSelectedOrNull() instanceof ItemEntry entry) {
            if (this.onFocusedAction != null) {
                this.onFocusedAction.run();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            if (this.hoveredElement(mouseX, mouseY).orElse(null) instanceof ItemEntry entry) {
                if (mouseX > this.getX() + this.itemHeight) {
                    // 点击列表文字
                    boolean bl = this.selectedItemEntry != entry;
                    if (bl) {
                        this.setSingleCheckedItem(entry);
                        this.selectedItemEntry = entry;
                    }
                    if (this.onEntryClicked != null) {
                        this.onEntryClicked.accept(entry, bl);
                    }
                } else {
                    // 点击列表checkBox
                    CheckState checkState = entry.getCheckState();
                    this.setSingleCheckedItem(entry);
                    entry.setCheckState(checkState != CheckState.CHECKED ? CheckState.CHECKED : CheckState.UNCHECKED);

                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


//    private void onEntryClicked(ItemEntry entry, boolean entryChanged) {
//
//    }

}
