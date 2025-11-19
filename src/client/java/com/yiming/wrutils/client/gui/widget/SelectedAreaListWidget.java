package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.client.gui.SubAreaEditScreen;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.*;


@Environment(EnvType.CLIENT)
public class SelectedAreaListWidget extends AlwaysSelectedEntryListWidget<SelectedAreaListWidget.Entry> {
    private final Map<SelectBox, OneAreaEntry> oneAreaEntriesMap = new LinkedHashMap<>();
    private final Set<String> areaNames = new HashSet<>();
    private SelectBoxes selectBoxes;
    private final Screen parent;

    public SelectedAreaListWidget(Screen parent, MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom);
        this.parent = parent;
    }

    public void updateEntries() {
        this.clearEntries();
        this.oneAreaEntriesMap.forEach((selectBox, entry) -> {
            this.addEntry(entry);
        });
        this.setSelected(selectBoxes.getCurrentSelectBox());
        this.refreshScroll();
    }


    public void setAreaEntries(SelectBoxes selectBoxes) {
        this.selectBoxes = selectBoxes;
        this.oneAreaEntriesMap.clear();
        for (SelectBox selectBox : selectBoxes.getList()) {
            addAreaEntry(selectBox);
        }

        this.updateEntries();
    }

    public void appendAreaEntry(SelectBox selectBox) {
        this.addAreaEntry(selectBox);
        this.updateEntries();
        this.setSelected(selectBox);
        this.setScrollY(this.getMaxScrollY());
    }

    public void addAreaEntry(SelectBox selectBox) {
        String name = selectBox.getName();
        int i = 1;
        while (this.areaNames.contains(name)) {
            name = "Sub-Area-" + i++;
        }
        this.areaNames.add(name);
        OneAreaEntry entry = new OneAreaEntry(selectBox, name, this.width, this.height);
        entry.setDeleteAction(() -> deleteAction(entry));
        entry.setEditAction(() -> editAction(entry));
        this.oneAreaEntriesMap.put(selectBox, entry);
    }

    public void removeAreaEntry(Entry entry) {
        if (entry instanceof OneAreaEntry entry1) {
            this.removeAreaEntry(entry1.getSelectBox());
        }
    }

    public void removeAreaEntry(SelectBox selectBox) {
        this.oneAreaEntriesMap.remove(selectBox);
        this.selectBoxes.remove(selectBox);
        this.areaNames.remove(selectBox.getName());
    }

    public void setSelected(SelectBox selectBox) {
        if (selectBox != null) {
            this.setSelected(oneAreaEntriesMap.get(selectBox));
        } else {
            this.setSelected((Entry) null);
        }
    }

    public SelectBoxes getSelectBoxes() {
        return selectBoxes;
    }


    private void deleteAction(OneAreaEntry entry) {
        this.removeAreaEntry(entry);
        this.updateEntries();
        // Todo: 删除对应在Server中的对象

    }

    public void editAction(OneAreaEntry entry) {
        this.client.setScreen(new SubAreaEditScreen(this.parent, entry, areaNames));
    }

    @Override
    public void setSelected(Entry entry) {
        super.setSelected(entry);
        if (entry instanceof OneAreaEntry entry1) {
            this.selectBoxes.setCurrentSelectBox(entry1.getSelectBox());
        }
    }

    @Override
    public int getRowWidth() {
        return Math.max(this.width - 30, 10);
    }

    @Override
    protected int getScrollbarX() {
        return this.getRight() - 10;
    }

    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<SelectedAreaListWidget.Entry> {

    }


    @Environment(EnvType.CLIENT)
    public static class OneAreaEntry extends SelectedAreaListWidget.Entry {
        private final Text name;
        private final int width;
        private final int height;
        private final ButtonWidget deleteButton;
        private final ButtonWidget editButton;
        private final SelectBox selectBox;
        private long lastClickTime = 0;
        private final MinecraftClient client;

        private Runnable deleteAction;
        private Runnable editAction;

        public OneAreaEntry(SelectBox selectBox, String name, int width, int height) {
            super();
            this.client = MinecraftClient.getInstance();
            this.width = width;
            this.height = height;
            this.name = Text.of(name);
            this.deleteButton = ButtonWidget.builder(Text.of("x"), button -> {
                if (deleteAction != null) {
                    deleteAction.run();
                }
            }).dimensions(0, 0, 20, 20).build();
            this.editButton = ButtonWidget.builder(Text.of("Edit"), button -> {
                if (editAction != null) {
                    editAction.run();
                }
            }).dimensions(0, 0, 40, 20).build();
            if (!selectBox.getName().equals(name)) {
                selectBox.setName(name);
            }
            this.selectBox = selectBox;
        }

        public void setDeleteAction(Runnable deleteAction) {
            this.deleteAction = deleteAction;
        }

        public void setEditAction(Runnable editAction) {
            this.editAction = editAction;
        }


        public SelectBox getSelectBox() {
            return selectBox;
        }

        @Override
        public Text getNarration() {
            return Text.of("One Area Entry");
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (this.client.options.getTouchscreen().getValue() || hovered) {
                if (this.isFocused()) {
                    context.fill(x - 1, y - 1, x + Math.max(this.width - 30, 10) - 3, y + 24 - 3, 0x20FFFFFF);
                } else {
                    context.fill(x - 2, y - 2, x + Math.max(this.width - 30, 10) - 2, y + 24 - 2, 0x20FFFFFF);
                }
            }

            this.deleteButton.setPosition(this.width - 50, y);
            this.deleteButton.render(context, mouseX, mouseY, tickDelta);

            this.editButton.setPosition(this.width - 100, y);
            this.editButton.render(context, mouseX, mouseY, tickDelta);

            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, name, 30, y + entryHeight / 2 - 9 / 2, Colors.WHITE);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (deleteButton.mouseClicked(mouseX, mouseY, button)) {
                return true; // 如果点击到了按钮，就消费事件
            }
            if (editButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }

            if (button == 0) { // 左键点击
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 200) {
                    if (editAction != null) {
                        editAction.run();
                    }
                    lastClickTime = 0;
                    return true;
                }
                lastClickTime = currentTime;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }


    }


}
