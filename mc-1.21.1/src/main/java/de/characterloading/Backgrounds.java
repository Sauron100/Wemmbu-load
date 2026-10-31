package de.characterloading;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** Loads the artwork from the mod jar (independent of resource reloads) and draws it. */
public final class Backgrounds {
    public static final int TEX_W = 1600, TEX_H = 900;
    private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

    private Backgrounds() {}

    public static ResourceLocation get(String file) {
        ResourceLocation cached = CACHE.get(file);
        if (cached != null) return cached;
        try (InputStream in = Backgrounds.class.getResourceAsStream(
                "/assets/" + CharacterLoadingClient.MOD_ID + "/backgrounds/" + file + ".png")) {
            if (in == null) return null;
            NativeImage image = NativeImage.read(in);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CharacterLoadingClient.MOD_ID, "bg/" + file);
            Minecraft.getInstance().getTextureManager().register(id, new DynamicTexture(image));
            CACHE.put(file, id);
            return id;
        } catch (Exception e) {
            System.err.println("[characterloading] Could not load " + file + ": " + e);
            return null;
        }
    }

    /** Draws the texture "cover"-style (fills the screen, keeps aspect ratio). */
    public static void drawCover(GuiGraphics g, ResourceLocation id, int w, int h, float alpha) {
        if (id == null || alpha <= 0f) return;
        double s = Math.max((double) w / TEX_W, (double) h / TEX_H);
        int dw = (int) Math.ceil(TEX_W * s), dh = (int) Math.ceil(TEX_H * s);
        int x = (w - dw) / 2, y = (h - dh) / 2;
        RenderSystem.enableBlend();
        g.setColor(1f, 1f, 1f, Math.min(1f, alpha));
        g.blit(id, x, y, dw, dh, 0f, 0f, TEX_W, TEX_H, TEX_W, TEX_H);
        g.setColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    public static Figure.Slot slotFor(Screen screen) {
        if (screen instanceof TitleScreen || screen instanceof ConfigScreen) return Figure.Slot.TITLE;
        if (screen instanceof ConnectScreen || screen instanceof ProgressScreen
                || screen instanceof ReceivingLevelScreen) return Figure.Slot.LOADING;
        if (screen instanceof SelectWorldScreen || screen instanceof JoinMultiplayerScreen) return Figure.Slot.MENU_A;
        return Figure.Slot.MENU_B;
    }

    public static boolean active() {
        Config c = Config.get();
        if (!c.enabled) return false;
        return Minecraft.getInstance().level == null || c.inWorld;
    }

    /** Draws the menu background for a screen. Returns true if we handled it. */
    public static boolean drawFor(Screen screen, GuiGraphics g) {
        if (!active()) return false;
        ResourceLocation id = get(Config.get().character().file(slotFor(screen)));
        if (id == null) return false;
        drawCover(g, id, screen.width, screen.height, 1f);
        g.fillGradient(0, 0, screen.width, screen.height, 0x30000000, 0x70000000);
        return true;
    }
}
