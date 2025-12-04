package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.GameTickEventsListWidget;
import com.yiming.wrutils.client.gui.widget.search.dropdown.item.ItemTextFieldListWidget;
import com.yiming.wrutils.client.gui.widget.search.SearchWidget;
import com.yiming.wrutils.data.DataManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Function;

public class GTEventsListScreen extends AbstractSetupScreen {
    private GameTickEventsListWidget gameTickEventsListWidget;
    private SearchWidget searchWidget;
    //    private ItemListWidget itemListWidget;
    private ItemTextFieldListWidget itemTextFieldListWidget;

    protected GTEventsListScreen(Screen parent) {
        super(Text.of("Game Tick Events"), parent, false);
    }

    private boolean handleMouseEvent(Function<Element, Boolean> handler) {
        if (handler.apply(this.searchWidget)) return true;
        if (handler.apply(this.gameTickEventsListWidget)) return true;
        return false;
    }


    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        super.init();
        int y = 26;


        this.gameTickEventsListWidget = new GameTickEventsListWidget(this, 0, 80, this.width, this.height - 64 - 30);
        this.gameTickEventsListWidget.setEvents(DataManager.eventRecorder);
        this.addDrawableChild(this.gameTickEventsListWidget);

        this.searchWidget = new SearchWidget(0, y, this.width, this.height - y - 10, Text.of("Search"));
        this.addDrawableChild(this.searchWidget);

//        ArrayList<String> list = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape", "Honeydew", "Iceberg"));
//        this.itemTextFieldListWidget = new ItemTextFieldListWidget(MinecraftClient.getInstance(), 100, 100, 10, y, 18);
//        this.itemTextFieldListWidget.setItemEntries(list);
//        this.addDrawableChild(this.itemTextFieldListWidget);

    }


    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseClicked(mouseX, mouseY, button));
        return bl || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean bl = handleMouseEvent(widget -> widget.mouseReleased(mouseX, mouseY, button));
        return bl || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean bl = handleMouseEvent(widget -> widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        return bl || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean bl = handleMouseEvent(widget -> widget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        return bl || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        boolean bl = handleMouseEvent(widget -> widget.charTyped(chr, keyCode));
        return bl || super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = handleMouseEvent(widget -> widget.keyPressed(keyCode, scanCode, modifiers));
        return bl || super.keyPressed(keyCode, scanCode, modifiers);
    }

}
