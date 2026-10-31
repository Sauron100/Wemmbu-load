package de.characterloading;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** Loads the artwork from the mod jar (independent of resource reloads) and draws it. */
public final class Backgrounds {
    public static final int TEX_W = 1600, TEX_H = 900;
    private static final Map<String, Identifier> CACHE = new HashMap<>();

    private Backgrounds() {}

    public static Identifier get(String file) {
        Identifier cached = CACHE.get(file);
        if (cached != null) return cached;
        try (InputStream in = Backgrounds.class.getResourceAsStream(
                "/assets/" + CharacterLoadingClient.MOD_ID + "/backgrounds/" + file + ".png")) {
            if (in == null) return null;
            NativeImage image = NativeImage.read(in);
            Identifier id = Identifier.of(CharacterLoadingClient.MOD_ID, "bg/" + file);
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, new NativeImageBackedTexture(image));
            CACHE.put(file, id);
            return id;
        } catch (Exception e) {
            System.err.println("[characterloading] Could not load " + file + ": " + e);
            return null;
        }
    }

    /** Draws the texture "cover"-style (fills the screen, keeps aspect ratio). */
    public static void drawCover(DrawContext ctx, Identifier id, int w, int h, float alpha) {
        if (id == null || alpha <= 0f) return;
        double s = Math.max((double) w / TEX_W, (double) h / TEX_H);
        int dw = (int) Math.ceil(TEX_W * s), dh = (int) Math.ceil(TEX_H * s);
        int x = (w - dw) / 2, y = (h - dh) / 2;
        RenderSystem.enableBlend();
        ctx.setShaderColor(1f, 1f, 1f, Math.min(1f, alpha));
        ctx.drawTexture(id, x, y, dw, dh, 0f, 0f, TEX_W, TEX_H, TEX_W, TEX_H);
        ctx.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    public static Figure.Slot slotFor(Screen screen) {
        if (screen instanceof TitleScreen || screen instanceof de.characterloading.ConfigScreen) return Figure.Slot.TITLE;
        if (screen instanceof ConnectScreen || screen instanceof ProgressScreen
                || screen instanceof DownloadingTerrainScreen) return Figure.Slot.LOADING;
        if (screen instanceof SelectWorldScreen || screen instanceof MultiplayerScreen) return Figure.Slot.MENU_A;
        return Figure.Slot.MENU_B;
    }

    public static boolean active() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Config c = Config.get();
        if (!c.enabled) return false;
        return mc.world == null || c.inWorld;
    }

    /** Draws the menu background for a screen. Returns true if we handled it. */
    public static boolean drawFor(Screen screen, DrawContext ctx) {
        if (!active()) return false;
        Identifier id = get(Config.get().character().file(slotFor(screen)));
        if (id == null) return false;
        drawCover(ctx, id, screen.width, screen.height, 1f);
        // slight darkening so vanilla buttons stay readable
        ctx.fillGradient(0, 0, screen.width, screen.height, 0x30000000, 0x70000000);
        return true;
    }
}
