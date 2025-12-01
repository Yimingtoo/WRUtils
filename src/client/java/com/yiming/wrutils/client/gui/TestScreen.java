package com.yiming.wrutils.client.gui;

import com.yiming.wrutils.client.gui.widget.search.ItemListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TestScreen extends AbstractSetupScreen {
    ItemListWidget itemListWidget;

    protected TestScreen(Text title, Screen parent) {
        super(title, parent);

    }

    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        super.init();
        int y = 26;

//        ArrayList<String> list = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Durian", "Elderberry", "Fig", "Grape", "Honeydew", "Iceberg"));
//        this.itemListWidget = new ItemListWidget(MinecraftClient.getInstance(), 100, 100, 100,80, 20);
//        this.itemListWidget.setItemEntries(list);
//        this.addDrawableChild(this.itemListWidget);
    }

}
