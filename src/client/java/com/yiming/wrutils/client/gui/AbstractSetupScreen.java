package com.yiming.wrutils.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class AbstractSetupScreen extends Screen {
    protected Text title;
    protected Screen parent;
    protected boolean allButtons = true;


    protected AbstractSetupScreen(Text title, Screen parent) {
        super(Text.of(""));
        this.title = title;
        this.parent = parent;
    }

    protected AbstractSetupScreen(Text title, Screen parent, boolean allButtons) {
        this(title, parent);
        this.allButtons = allButtons;
    }


    @Override
    protected void init() {
        MinecraftClient client1 = MinecraftClient.getInstance();
        int y = 0;
        Text text = title.copy().styled(style -> style.withBold(true));
        TextWidget titleWidget = addDrawableChild(new TextWidget(text, this.textRenderer));
        SimplePositioningWidget.setPos(titleWidget, 8, y + 4, this.textRenderer.getWidth(text), 20);

        int buttonWidth = 50;
        int i = 0;
        ButtonWidget homeButton = this.addDrawableChild(ButtonWidget.builder(Text.of("Home"), button -> client1.setScreen(new MainMenuScreen())).width(buttonWidth).build());
        SimplePositioningWidget.setPos(homeButton, this.width - (buttonWidth + 5) * ++i, y + 4, buttonWidth, 20);

        if (this.allButtons) {
            ButtonWidget upperButton = this.addDrawableChild(ButtonWidget.builder(Text.of("Upper"), button -> this.upperScreen()).width(buttonWidth).build());
            SimplePositioningWidget.setPos(upperButton, this.width - (buttonWidth + 5) * ++i, y + 4, buttonWidth, 20);

            ButtonWidget backButton = this.addDrawableChild(ButtonWidget.builder(Text.of("Back"), button -> client1.setScreen(this.parent)).width(buttonWidth).build());
            SimplePositioningWidget.setPos(backButton, this.width - (buttonWidth + 5) * ++i, y + 4, buttonWidth, 20);

        }
    }

    protected void upperScreen() {
    }

}
