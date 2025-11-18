package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

@Environment(EnvType.CLIENT)
public class SelectedAreaListWidget extends AlwaysSelectedEntryListWidget<SelectedAreaListWidget.Entry> {
    List<SelectedAreaListWidget.OneAreaEntry> areaEntries = new ArrayList<>();
    Map<SelectBox, OneAreaEntry> oneAreaEntriesMap = new LinkedHashMap<>();
    SelectBoxes selectBoxes;
    Set<String> areaNames = new HashSet<>();

    public SelectedAreaListWidget(MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom);

    }

    public void updateEntries() {
        this.clearEntries();
        this.oneAreaEntriesMap.forEach((selectBox, entry) -> {
            this.addEntry(entry);
        });
        this.setSelected(selectBoxes.getCurrentBox());
        this.refreshScroll();
    }

    public void setAreaEntries(MinecraftClient minecraftClient) {
        this.areaEntries.clear();

        List<Text> texts = newArrayList();
        texts.add(Text.of("Apple1"));
        texts.add(Text.of("Banana"));
        texts.add(Text.of("Cherry"));
        texts.add(Text.of("Durian"));
        texts.add(Text.of("Apple1"));
        texts.add(Text.of("Banana"));
        texts.add(Text.of("Cherry"));
        texts.add(Text.of("Durian"));
        texts.add(Text.of("Apple1"));
        texts.add(Text.of("Banana"));
        texts.add(Text.of("Cherry"));
        texts.add(Text.of("Durian"));
        for (Text text : texts) {
            OneAreaEntry entry = new OneAreaEntry(text, this.width, this.height);
            entry.setDeleteAction(() -> deleteAction(entry));
            this.areaEntries.add(entry);
        }

        this.updateEntries();
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


    private void deleteAction(OneAreaEntry entry) {
        this.removeAreaEntry(entry);
        this.updateEntries();
        // Todo: 删除对应在Server中的对象

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

    @Deprecated
    public void clearAreaEntries() {
        this.areaEntries.clear();
        this.updateEntries();

    }

    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<SelectedAreaListWidget.Entry> {

    }


    @Environment(EnvType.CLIENT)
    public static class OneAreaEntry extends SelectedAreaListWidget.Entry {

        private final Text name;
        private int width;
        private int height;
        Runnable deleteAction;
        Runnable openRenameDialogAction;
        ButtonWidget deleteButton;
        SelectBox selectBox;
        private long lastClickTime = 0;
        private boolean isEditing = false;

        public OneAreaEntry(SelectBox selectBox, String name, int width, int height) {
            this(Text.of(name), width, height);
            if (!selectBox.getName().equals(name)) {
                selectBox.setName(name);
            }
            this.selectBox = selectBox;
        }

        public OneAreaEntry(Text text, int width, int height) {
            super();
            this.width = width;
            this.height = height;
            this.name = text;
            this.deleteButton = ButtonWidget.builder(Text.of("x"), button -> {
                deleteAction.run();
            }).dimensions(0, 0, 20, 20).build();
        }

        public void setDeleteAction(Runnable deleteAction) {
            this.deleteAction = deleteAction;
        }

        public void setOpenRenameDialogAction(Runnable openRenameDialogAction) {
            this.openRenameDialogAction = openRenameDialogAction;
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
            this.deleteButton.setPosition(this.width - 50, y);
            this.deleteButton.render(context, mouseX, mouseY, tickDelta);

            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, name, 30, y + entryHeight / 2 - 9 / 2, Colors.WHITE);

            if (MinecraftClient.getInstance().options.getTouchscreen().getValue() || hovered) {
                if (this.isFocused()) {
//                    context.fill(x - 1, y - 1, x + Math.max(this.width - 30, 10) - 3, y + 24 - 3, 0xFF808080);
                    context.fill(x - 1, y - 1, x + Math.max(this.width - 30, 10) - 3, y + 24 - 3, 0x20FFFFFF);
                } else {
                    context.fill(x - 2, y - 2, x + Math.max(this.width - 30, 10) - 2, y + 24 - 2, 0x20FFFFFF);
                }
            }

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (deleteButton.mouseClicked(mouseX, mouseY, button)) {
                return true; // 如果点击到了按钮，就消费事件
            }

            if (button == 0) { // 左键点击
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 200) {
                    System.out.println("Double Click");
                    lastClickTime = 0;
                    return true;
                }
                lastClickTime = currentTime;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }


    }


}
