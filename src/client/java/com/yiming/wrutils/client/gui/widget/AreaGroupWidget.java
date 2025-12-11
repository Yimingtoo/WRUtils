package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.client.gui.AreaListScreen;
import com.yiming.wrutils.data.selected_area.AreaGroupManagement;
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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// 1111

@Environment(EnvType.CLIENT)
public class AreaGroupWidget extends AlwaysSelectedEntryListWidget<AreaGroupWidget.Entry> {
    private final Map<SelectBoxes, OneGroupEntry> oneGroupEntriesMap = new LinkedHashMap<>();
    private final Set<String> groupNames = new HashSet<>();
    private AreaGroupManagement areaGroupManagement;
    private final Screen parent;

    public AreaGroupWidget(Screen parent, MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom);
        this.parent = parent;
    }

    public void updateEntries() {
        this.clearEntries();
        this.oneGroupEntriesMap.forEach((selectBoxes, entry) -> this.addEntry(entry));
        this.setSelected(areaGroupManagement.getCurrentBoxes());
        this.refreshScroll();
    }


    public void setAreaEntries(AreaGroupManagement areaGroupManagement) {
        this.areaGroupManagement = areaGroupManagement;
        this.oneGroupEntriesMap.clear();
        for (SelectBoxes selectBoxes : areaGroupManagement.getList()) {
            addAreaEntry(selectBoxes);
        }

        this.updateEntries();
    }

    public void appendAreaEntry(SelectBoxes selectBoxes) {
        this.addAreaEntry(selectBoxes);
        this.updateEntries();
        this.setSelected(selectBoxes);
        this.setScrollY(this.getMaxScrollY());
    }

    public void addAreaEntry(SelectBoxes selectBoxes) {
        String name = selectBoxes.getName();
        int i = 1;
        while (this.groupNames.contains(name)) {
            name = "Area-" + i++;
        }
        this.groupNames.add(name);
        OneGroupEntry entry = new OneGroupEntry(selectBoxes, name, this.width, this.height);
        entry.setDeleteAction(() -> deleteAction(entry));
        entry.setEditAction(() -> editAction(entry));
        this.oneGroupEntriesMap.put(selectBoxes, entry);
    }

    public void removeAreaEntry(Entry entry) {
        if (entry instanceof OneGroupEntry entry1) {
            this.removeAreaEntry(entry1.getSelectBoxes());
        }
    }

    public void removeAreaEntry(SelectBoxes selectBoxes) {
        this.oneGroupEntriesMap.remove(selectBoxes);
        this.areaGroupManagement.remove(selectBoxes);
        this.groupNames.remove(selectBoxes.getName());
    }

    public void setSelected(SelectBoxes selectBoxes) {
        if (selectBoxes != null) {
            this.setSelected(oneGroupEntriesMap.get(selectBoxes));
        } else {
            this.setSelected((Entry) null);
        }
    }

    public AreaGroupManagement getAreaGroupManagement() {
        return areaGroupManagement;
    }


    private void deleteAction(OneGroupEntry entry) {
        this.removeAreaEntry(entry);
        this.updateEntries();
    }

    public void editAction(OneGroupEntry entry) {
        this.client.setScreen(new AreaListScreen(this.parent, entry.getSelectBoxes()));
    }


    @Override
    public void setSelected(Entry entry) {
        super.setSelected(entry);
        if (entry instanceof OneGroupEntry entry1) {
            this.areaGroupManagement.setCurrentSelectBox(entry1.getSelectBoxes());
        } else if (entry == null) {
            this.areaGroupManagement.setCurrentSelectBox(null);
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

    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<AreaGroupWidget.Entry> {

    }

    @Environment(EnvType.CLIENT)
    public static class OneGroupEntry extends AreaGroupWidget.Entry {
        private final Text name;
        private final int width;
        private final int height;
        private final ButtonWidget deleteButton;
        private final ButtonWidget editButton;
        private final SelectBoxes selectBoxes;
        private long lastClickTime = 0;
        private final MinecraftClient client;

        private Runnable deleteAction;
        private Runnable editAction;


        public OneGroupEntry(SelectBoxes selectBoxes, String name, int width, int height) {
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
            if (!selectBoxes.getName().equals(name)) {
                selectBoxes.setName(name);
            }
            this.selectBoxes = selectBoxes;
        }

        public void setDeleteAction(Runnable deleteAction) {
            this.deleteAction = deleteAction;
        }

        public void setEditAction(Runnable editAction) {
            this.editAction = editAction;
        }


        public SelectBoxes getSelectBoxes() {
            return this.selectBoxes;
        }

        @Override
        public Text getNarration() {
            return Text.of("One Group Entry");
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (this.client.options.getTouchscreen().getValue() || hovered) {
                if (this.isFocused()) {
                    context.fill(x - 1, y - 1, x + Math.max(this.width - 30, 10) - 3, y + 30 - 3, 0x20FFFFFF);
                } else {
                    context.fill(x - 2, y - 2, x + Math.max(this.width - 30, 10) - 2, y + 30 - 2, 0x20FFFFFF);
                }
            }

            this.deleteButton.setPosition(this.width - 50, y + 3);
            this.deleteButton.render(context, mouseX, mouseY, tickDelta);

            this.editButton.setPosition(this.width - 100, y + 3);
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
