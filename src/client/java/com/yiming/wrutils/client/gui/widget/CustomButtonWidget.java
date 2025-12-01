package com.yiming.wrutils.client.gui.widget;

import com.yiming.wrutils.data.DataManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class CustomButtonWidget extends ClickableWidget {
    private final int margin;
    private final int halfHeight;
    private final ButtonWidget button1;
    private final ButtonWidget button2;
    private int button1PosY;
    private int button2PosY;

//    private boolean isEnabled = false;

    TextWidget textWidget;
    TextWidget textWidget1;
    TextWidget textWidget2;
    int textWidgetPosY;
    int textWidget1PosY;
    int textWidget2PosY;

    private int level;

    private Runnable onClickAction;


    public CustomButtonWidget(int x, int y, int maxWidth, int minHeight, int margin, Text message) {
        super(x, y, maxWidth, 6 * (minHeight / 2) + 4 * margin, message);
        int halfHeight = minHeight / 2;
        button1 = new ButtonWidget(0, 0, maxWidth - margin * 2, (halfHeight * 2 + margin) * 2, Text.of("Unset"));
        button2 = new ButtonWidget(0, 0, maxWidth - margin * 4, halfHeight * 2, Text.of("Unset"));

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textWidget = new TextWidget(0, 0, maxWidth, halfHeight * 2, Text.of("Unset"), textRenderer);
        textWidget1 = new TextWidget(0, 0, maxWidth, halfHeight * 2, Text.of("Unset"), textRenderer);
        textWidget2 = new TextWidget(0, 0, maxWidth, halfHeight * 2, Text.of("Unset"), textRenderer);

        this.margin = margin;
        this.halfHeight = halfHeight;
        this.level = 1;

        this.setMessage(Text.of("OutSide"));
        this.button1.setMessage(Text.of("Middle"));
        this.button2.setMessage(Text.of("Inside"));

        this.active = DataManager.isSelectionEnabled;
        this.button1.active = DataManager.isSelectionEnabled;
        this.button2.active = DataManager.isSelectionEnabled;

    }

    public void setText(String text, String text1, String text2) {
        int maxWidth = this.getWidth() - this.margin * 4;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String tempText = text;
        String trimText = textRenderer.trimToWidth(tempText, maxWidth);
        this.textWidget.setMessage(Text.of(trimText.equals(tempText) ? tempText : trimText + "..."));
        maxWidth -= this.margin * 2;
        tempText = text1;
        trimText = textRenderer.trimToWidth(tempText, maxWidth);
        this.textWidget1.setMessage(Text.of(trimText.equals(tempText) ? tempText : trimText + "..."));
        maxWidth -= this.margin * 2;
        tempText = text2;
        trimText = textRenderer.trimToWidth(tempText, maxWidth);
        this.textWidget2.setMessage(Text.of(trimText.equals(tempText) ? tempText : trimText + "..."));
    }

    private void showButtonLevel() {
        switch (this.level) {
            case 1:
                this.button1.visible = false;
                this.button2.visible = false;
                this.textWidget1.visible = false;
                this.textWidget2.visible = false;
                this.textWidgetPosY = this.getY() + this.halfHeight * 2 + this.margin * 2;
                break;
            case 2:
                this.button1.visible = true;
                this.button2.visible = false;
                this.button1.setHeight(this.halfHeight * 3 + this.margin);
                this.button1PosY = this.getY() + this.halfHeight * 3 + this.margin * 2;
                this.textWidget1.visible = true;
                this.textWidget2.visible = false;
                this.textWidgetPosY = this.getY() + this.halfHeight / 2 + this.margin * 2;
                this.textWidget1PosY = this.getY() + this.halfHeight * 4 + this.margin * 2;
                break;
            case 3:
                this.button1.visible = true;
                this.button2.visible = true;
                this.button1.setHeight(this.halfHeight * 4 + this.margin * 2);
                this.button1PosY = this.getY() + this.halfHeight * 2 + this.margin;
                this.button2PosY = this.getY() + (this.halfHeight * 2 + this.margin) * 2;
                this.textWidget1.visible = true;
                this.textWidget2.visible = true;
                this.textWidgetPosY = this.getY() + this.margin / 2;
                this.textWidget1PosY = this.getY() + this.halfHeight * 2 + this.margin + this.margin / 2;
                this.textWidget2PosY = this.getY() + this.halfHeight * 4 + this.margin * 2 + this.margin / 2;
                break;
            default:
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setOnClickAction(Runnable onClickAction, Runnable onClickAction1, Runnable onClickAction2) {
        this.onClickAction = onClickAction;
        this.button1.setOnClickAction(onClickAction1);
        this.button2.setOnClickAction(onClickAction2);
    }

    public void setEnabled(boolean enabled) {
        this.active = enabled;
        this.button1.active = enabled;
        this.button2.active = enabled;
    }


    private static void renderButton(ClickableWidget widget, DrawContext context, boolean highlight) {
        context.fill(widget.getX(), widget.getY(), widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(), highlight ? 0xFFFFFFFF : 0xFFA0A0A0);
        context.fill(widget.getX() + 1, widget.getY() + 1, widget.getX() + widget.getWidth() - 1, widget.getY() + widget.getHeight() - 1, 0xFF000000);

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.showButtonLevel();

        boolean isHighlight = this.isHovered() && !this.button1.isHovered() && !this.button2.isHovered() && this.active;
        CustomButtonWidget.renderButton(this, context, isHighlight);
        this.button1.setHighlight(this.button1.isHovered() && !this.button2.isHovered());
        this.button2.setHighlight(this.button2.isHovered());

        this.button1.setPosition(this.getX() + this.margin, this.button1PosY);
        this.button1.render(context, mouseX, mouseY, delta);

        this.button2.setPosition(this.getX() + this.margin * 2, this.button2PosY);
        this.button2.render(context, mouseX, mouseY, delta);

        this.textWidget.setPosition(this.getX(), this.textWidgetPosY);
        this.textWidget.render(context, mouseX, mouseY, delta);
        this.textWidget1.setPosition(this.getX(), this.textWidget1PosY);
        this.textWidget1.render(context, mouseX, mouseY, delta);
        this.textWidget2.setPosition(this.getX(), this.textWidget2PosY);
        this.textWidget2.render(context, mouseX, mouseY, delta);

        if (!this.active) {
            context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0x20FFFFFF);
        }


    }


    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
//        System.out.println(this.getMessage().getString() + " clicked");
        if (this.onClickAction != null) {
            this.onClickAction.run();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.button2.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.button1.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private static class ButtonWidget extends ClickableWidget {
        private boolean shouldHighlight;
        private Runnable onClickAction;


        public ButtonWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        public void setHighlight(boolean shouldHighlight) {
            this.shouldHighlight = shouldHighlight;
        }

        public void setOnClickAction(Runnable onClickAction) {
            this.onClickAction = onClickAction;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            CustomButtonWidget.renderButton(this, context, this.shouldHighlight && this.active);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            System.out.println(this.getMessage().getString() + " clicked");
            if (this.onClickAction != null) {
                this.onClickAction.run();
            }
        }

    }

}
