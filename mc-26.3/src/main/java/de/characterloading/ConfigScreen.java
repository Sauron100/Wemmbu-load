package de.characterloading;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.EnumMap;
import java.util.Map;

/** Character picker, opened from Mod Menu. */
public class ConfigScreen extends Screen {
    private final Screen parent;
    private final Map<Figure, Button> figureButtons = new EnumMap<>(Figure.class);
    private Button enabledButton, inWorldButton;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Character Loading Screens"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int colW = Math.min(240, (int) (width * 0.40));
        int colX = (int) (width * 0.58);
        int y = height / 2 - 40;
        for (Figure f : Figure.values()) {
            Button b = Button.builder(Component.empty(), btn -> {
                Config.get().character = f.name();
                Config.save();
                refresh();
            }).bounds(colX, y, colW, 24).build();
            figureButtons.put(f, addRenderableWidget(b));
            y += 28;
        }
        y += 12;
        enabledButton = addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.get().enabled = !Config.get().enabled; refresh();
        }).bounds(colX, y, colW, 20).build());
        inWorldButton = addRenderableWidget(Button.builder(Component.empty(), b -> {
            Config.get().inWorld = !Config.get().inWorld; refresh();
        }).bounds(colX, y + 24, colW, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(colX, y + 54, colW, 20).build());
        refresh();
    }

    private void refresh() {
        for (Map.Entry<Figure, Button> e : figureButtons.entrySet()) {
            boolean sel = Config.get().character() == e.getKey();
            e.getValue().setMessage(Component.literal((sel ? "> PLAY AS " : "- PLAY AS ") + e.getKey().display.toUpperCase()));
        }
        enabledButton.setMessage(Component.literal("Custom backgrounds: " + (Config.get().enabled ? "ON" : "OFF")));
        inWorldButton.setMessage(Component.literal("Also in pause menu: " + (Config.get().inWorld ? "ON" : "OFF")));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        Component title = Component.literal("CHOOSE YOUR CHARACTER");
        int cx = (int) (width * 0.58) + Math.min(240, (int) (width * 0.40)) / 2;
        g.text(this.font, title, cx - this.font.width(title) / 2, height / 2 - 66, 0xFFE8E8E8);
    }

    @Override
    public void onClose() {
        Config.save();
        this.minecraft.setScreenAndShow(parent);
    }
}
