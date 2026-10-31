package de.characterloading;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

/** Figure picker, styled like the "PLAY AS ..." list in the edits. Opened from Mod Menu. */
public class ConfigScreen extends Screen {
    private final Screen parent;
    private ButtonWidget enabledButton, inWorldButton;

    public ConfigScreen(Screen parent) {
        super(Text.literal("Figure Loading Screens"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int colX = (int) (width * 0.56), colW = (int) (width * 0.40);
        int y = height / 2 - 40;
        for (Figure c : Figure.values()) {
            addDrawableChild(new CharacterButton(colX, y, colW, 28, c));
            y += 34;
        }
        y += 14;
        int bw = Math.min(200, colW);
        int bx = colX + (colW - bw) / 2;
        enabledButton = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
            Config.get().enabled = !Config.get().enabled; refresh();
        }).dimensions(bx, y, bw, 20).build());
        inWorldButton = addDrawableChild(ButtonWidget.builder(Text.empty(), b -> {
            Config.get().inWorld = !Config.get().inWorld; refresh();
        }).dimensions(bx, y + 24, bw, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(bx, y + 54, bw, 20).build());
        refresh();
    }

    private void refresh() {
        enabledButton.setMessage(Text.literal("Custom backgrounds: " + (Config.get().enabled ? "ON" : "OFF")));
        inWorldButton.setMessage(Text.literal("Also in pause menu: " + (Config.get().inWorld ? "ON" : "OFF")));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int cx = (int) (width * 0.76);
        ctx.getMatrices().push();
        ctx.getMatrices().scale(2f, 2f, 1f);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("CHOOSE YOUR CHARACTER"), cx / 2, (height / 2 - 100) / 2, 0xFFE8E8E8);
        ctx.getMatrices().pop();
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Backgrounds & loading screen change instantly"),
                cx, height / 2 - 72, 0xFFA0A0A0);
    }

    @Override
    public void close() {
        Config.save();
        MinecraftClient.getInstance().setScreen(parent);
    }

    private static class CharacterButton extends ClickableWidget {
        private final Figure character;

        CharacterButton(int x, int y, int w, int h, Figure character) {
            super(x, y, w, h, Text.literal(character.display));
            this.character = character;
        }

        @Override
        protected void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
            boolean selected = Config.get().character() == character;
            int color = selected ? 0xFFFFFFFF : isHovered() ? 0xFFFFE9A8 : 0xFFB8B8B8;
            String label = (selected ? "> PLAY AS " : "- PLAY AS ") + character.display.toUpperCase();
            var tr = MinecraftClient.getInstance().textRenderer;
            ctx.getMatrices().push();
            float sc = 1.6f;
            ctx.getMatrices().scale(sc, sc, 1f);
            ctx.drawCenteredTextWithShadow(tr, Text.literal(label),
                    (int) ((getX() + getWidth() / 2f) / sc), (int) ((getY() + (getHeight() - 8 * sc) / 2f) / sc), color);
            ctx.getMatrices().pop();
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            Config.get().character = character.name();
            Config.save();
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            appendDefaultNarrations(builder);
        }
    }
}
