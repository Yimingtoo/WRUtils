package com.yiming.wrutils.client.gui.widget.filter.dropdown.item;

import com.yiming.wrutils.client.gui.widget.filter.dropdown.CheckState;
import com.yiming.wrutils.client.gui.widget.filter.item.FilterType;
import net.minecraft.client.MinecraftClient;

public class SingleSelectItemListWidget extends ItemListWidget {
    private ItemEntry<? extends FilterType> selectedItem = null;

    public SingleSelectItemListWidget(MinecraftClient minecraftClient, int width, int x, int y, int itemHeight) {
        super(minecraftClient, width, x, y, itemHeight);
    }

    public void setSingleCheckedItem(ItemEntry<? extends FilterType>  itemEntry) {
        this.children().forEach(entry -> {
            if (entry instanceof ItemEntry<? extends FilterType>  itemEntry1) {
                itemEntry1.setChecked(itemEntry1 == itemEntry);
            }
        });
    }



    @Override
    public void setCheckedItems(boolean checked) {
        if (!checked) {
            this.children().forEach(entry -> {
                if (entry instanceof ItemEntry<? extends FilterType>  itemEntry) {
                    itemEntry.setChecked(false);
                }
            });
        } else {
            if (this.selectedItem != null) {
                this.selectedItem.setChecked(true);
            }
        }
    }

    @Override
    protected void setOnFocused() {
        if (this.getSelectedOrNull() instanceof ItemEntry<? extends FilterType>  entry) {
            if (this.onFocusedAction != null) {
                this.onFocusedAction.run();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (mouseX < this.getX() + this.itemHeight) {
//            if (this.getFocused() instanceof ItemEntry entry) {
//                entry.setChecked(entry.getCheckState() != CheckState.CHECKED);
//            }
//        }

        if (this.hoveredElement(mouseX, mouseY).orElse(null) instanceof ItemEntry<? extends FilterType>  entry) {
            if (mouseX > this.getX() + this.itemHeight) {
                if (this.selectedItem != entry) {
                    this.setSingleCheckedItem(entry);
                    this.selectedItem = entry;
                }
            } else {
                CheckState checkState = entry.getCheckState();
                this.setSingleCheckedItem(entry);
                entry.setChecked(checkState != CheckState.CHECKED);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

}
