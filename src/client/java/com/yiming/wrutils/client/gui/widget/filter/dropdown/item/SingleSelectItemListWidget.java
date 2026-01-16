package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.gui.widget.filter.CheckState;
import net.minecraft.client.MinecraftClient;

public class SingleSelectItemListWidget extends ItemListWidget {
    private ItemEntry selectedItem = null;

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

    @Override
    public void reset() {
        if (this.getFirst() instanceof ItemEntry entry) {
            this.setSingleCheckedItem(entry);
            this.selectedItem = entry;
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
            if (this.selectedItem != null) {
                this.selectedItem.setCheckState(CheckState.CHECKED);
            } else if (!this.children().isEmpty() && this.getFirst() instanceof ItemEntry entry) {
                this.selectedItem = entry;
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
                    if (this.selectedItem != entry) {
                        this.setSingleCheckedItem(entry);
                        this.selectedItem = entry;
                    }
                } else {
                    CheckState checkState = entry.getCheckState();
                    this.setSingleCheckedItem(entry);
                    entry.setCheckState(checkState != CheckState.CHECKED ? CheckState.CHECKED : CheckState.UNCHECKED);

                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

}
