package com.yiming.wrutils.client.gui.widget;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Environment(EnvType.CLIENT)
public class SelectedAreaListWidget extends AlwaysSelectedEntryListWidget<SelectedAreaListWidget.Entry> {
    List<SelectedAreaListWidget.OneAreaEntry> areaEntries = new ArrayList<>();

    public SelectedAreaListWidget(MinecraftClient client, int width, int height, int top, int bottom) {
        super(client, width, height, top, bottom);
    }

    private void updateEntries() {
        this.clearEntries();
        this.areaEntries.forEach(this::addEntry);

    }

    public void setAreaEntries(MinecraftClient minecraftClient) {
        this.areaEntries.clear();

        System.out.println(this.width);

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
            this.areaEntries.add(new OneAreaEntry(text, this.width, this.height));
        }

        this.updateEntries();
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
        private final Text text;
        private int width;
        private int height;
        private boolean isSelected;

        public OneAreaEntry(Text text, int width, int height) {
            super();
            this.width = width;
            this.height = height;
            this.text = text;
        }

        @Override
        public Text getNarration() {
            return Text.of("One Area Entry");

        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer, text, this.width / 2, y + entryHeight / 2 - 9 / 2, Colors.WHITE
            );
            if (MinecraftClient.getInstance().options.getTouchscreen().getValue() || hovered) {
                if (this.isFocused()) {
                    context.fill(x - 1, y - 1, x + Math.max(this.width - 30, 10) - 3, y + 24 - 3, 0x20FFFFFF);
                } else {
                    context.fill(x - 2, y - 2, x + Math.max(this.width - 30, 10) - 2, y + 24 - 2, 0x20FFFFFF);
                }
            }


        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            isSelected = true;
            return super.mouseClicked(mouseX, mouseY, button);
        }

    }


}
