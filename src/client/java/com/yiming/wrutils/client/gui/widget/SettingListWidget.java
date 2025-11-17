package com.yiming.wrutils.client.gui.widget;

import com.google.common.collect.ImmutableList;
import com.yiming.wrutils.client.data.ConfigManager;
import com.yiming.wrutils.client.gui.SettingGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;

import java.util.List;

@Deprecated
@Environment(EnvType.CLIENT)
public class SettingListWidget extends ElementListWidget<SettingListWidget.Entry> {
    private static ConfigManager configManager = new ConfigManager("config/wrutils/wrutils.properties");

    private final SettingGui parent;

    public SettingListWidget(MinecraftClient client, SettingGui parent) {
        super(client, parent.width, parent.layout.getContentHeight(), parent.layout.getHeaderHeight(), 25);
        this.parent = parent;

        int countEntry = 3;
        int countEmptyEntry = (this.getHeight() - 10 > countEntry * 25 ? (this.getHeight() - this.getEntryCount() * 25) : 0) / (25 * 2) - 1;
        for (int i = 0; i < countEmptyEntry; i++) {
            this.addEntry(new EmptyEntry());
        }

        this.addEntry(new SettingEntry(Text.literal("项目1"), "option1"));
        this.addEntry(new SettingEntry(Text.literal("项目2"), "option2"));
        this.addEntry(new SettingEntry(Text.literal("项目3"), "option3"));

    }


    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<SettingListWidget.Entry> {
    }

    public class SettingEntry extends SettingListWidget.Entry {
        private final Text text;
        private final ButtonWidget button0;
        private boolean status;
        private final String id;

        public SettingEntry(Text text, String id) {
            this.status = configManager.getConfigOption(id);
            this.text = text;
            this.id = id;
            this.button0 = ButtonWidget.builder(this.getMessageText(), button -> {
                changeButtonText(button);
            }).dimensions(0, 0, 75, 20).build();
        }

//        public SettingEntry(Text text, String id) {
//            this(text, id);
//        }

        /**
         * 根据当前状态更改按钮文本
         * 此方法根据当前的 status 状态来决定按钮上显示的文本如果 status 为 true，则按钮文本设置为 "OFF"；
         * 否则，按钮文本设置为 "ON" 通过这种方式，用户可以直观地了解当前的功能状态
         *
         * @param buttonWidget ButtonWidget 类型的参数，代表需要更改文本的按钮对象
         */
        private void changeButtonText(ButtonWidget buttonWidget) {
            if (this.status) {
                this.status = false;
                buttonWidget.setMessage(this.getMessageText());
            } else {
                this.status = true;
                buttonWidget.setMessage(this.getMessageText());
            }
            configManager.setConfigOptionAndSave(this.id, this.status);

        }

        private Text getMessageText() {
            return Text.literal(this.status ? "ON" : "OFF").formatted(new Formatting[]{this.status ? Formatting.GREEN : Formatting.RED});
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.button0);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.button0);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int i = SettingListWidget.this.getScrollbarX() - 10;
            int j = y - 2;
            int k = i - 5 - this.button0.getWidth();
            this.button0.setPosition(k, j);
            this.button0.render(context, mouseX, mouseY, tickDelta);
            context.drawTextWithShadow(SettingListWidget.this.client.textRenderer, this.text, x, y + entryHeight / 2 - parent.getTextHeight() / 2, Colors.WHITE);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class EmptyEntry extends SettingListWidget.Entry {
        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        }
    }

}
