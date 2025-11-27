package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.data.event.BaseEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.ArrayList;

@Deprecated
public class GameTickEventsListWidget1 extends AlwaysSelectedEntryListWidget<GameTickEventsListWidget1.Entry> {
    private final Screen parent;
    private final ArrayList<Entry> entries = new ArrayList<>();


    public GameTickEventsListWidget1(Screen parent, MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.parent = parent;
    }

    public void setEntries(ArrayList<BaseEvent> baseEvents) {
        this.clearEntries();

        long gameTick = -1;
        GameTickEntry gameTickEntry = new GameTickEntry(gameTick);
        for (BaseEvent baseEvent : baseEvents) {
            if (baseEvent.getTimeStamp().gameTime() != gameTick) {
                gameTick = baseEvent.getTimeStamp().gameTime();
                gameTickEntry = new GameTickEntry(gameTick);
                this.addEntry(gameTickEntry);
            }
            EventEntry eventEntry = new EventEntry(baseEvent);
            gameTickEntry.addVisibleCtrl(eventEntry);
            this.addEntry(eventEntry);

        }
        this.entries.clear();
        this.entries.addAll(this.children());
        this.refreshScroll();
    }

    public void updateEntries() {
        this.clearEntries();

        for (Entry entry : this.entries) {
            if (entry instanceof EventEntry entry1) {
                if (entry1.isVisible) {
                    this.addEntry(entry1);
                }
            } else {
                this.addEntry(entry);
            }
        }
        this.refreshScroll();
    }

    @Override
    public void setSelected(Entry entry) {
        if (entry instanceof GameTickEntry gameTickEntry) {
            gameTickEntry.setVisibleCtrlList(!gameTickEntry.isFocused());
            this.updateEntries();
        }
        super.setSelected(entry);
    }


    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
    }


    public static class GameTickEntry extends Entry {
        ArrayList<VisibleCtrl> visibleCtrlList = new ArrayList<>();
        private long gameTick;

        public GameTickEntry(long gameTick) {
            this.gameTick = gameTick;
        }

        public void setVisibleCtrlList(boolean visible) {
            for (VisibleCtrl visibleCtrl : visibleCtrlList) {
                visibleCtrl.setVisible(visible);
            }
        }

        private void addVisibleCtrl(VisibleCtrl visibleCtrl) {
            this.visibleCtrlList.add(visibleCtrl);
        }

        @Override
        public Text getNarration() {
            return Text.of("GameTick Entry");
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, "Game Tick: " + gameTick, 30, y + entryHeight / 2 - 9 / 2, Colors.WHITE);

        }

    }

    private interface VisibleCtrl {
        void setVisible(boolean visible);
    }

    public static class EventEntry extends Entry implements VisibleCtrl {
        private boolean isVisible = true;
        private BaseEvent event;

        public EventEntry(BaseEvent event) {
            super();
            this.event = event;
        }

        @Override
        public Text getNarration() {
            return Text.of("Events Entry");
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.event.toString(), 30, y + entryHeight / 2 - 9 / 2, Colors.WHITE);

        }

        @Override
        public void setVisible(boolean visible) {
            this.isVisible = visible;
        }

        public boolean isVisible() {
            return this.isVisible;
        }

    }


}
