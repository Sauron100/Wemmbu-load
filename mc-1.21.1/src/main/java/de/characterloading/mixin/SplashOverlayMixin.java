package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import de.characterloading.Config;
import de.characterloading.Figure;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the character artwork over the Mojang start-up screen (and every F3+T reload).
 * While it fades out it cross-fades into the "transition" image, which melts into the title screen.
 */
@Mixin(LoadingOverlay.class)
public abstract class SplashOverlayMixin {
    @Shadow private long fadeOutStart;

    @Inject(method = "render", at = @At("TAIL"))
    private void characterloading$render(GuiGraphics g, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Config cfg = Config.get();
        if (!cfg.enabled) return;
        Figure ch = cfg.character();
        ResourceLocation loading = Backgrounds.get(ch.file(Figure.Slot.LOADING));
        if (loading == null) return;

        int w = g.guiWidth(), h = g.guiHeight();
        long now = Util.getMillis();
        float f = fadeOutStart > -1L ? (now - fadeOutStart) / 1000f : -1f;
        float globalAlpha = f >= 1f ? 1f - Mth.clamp(f - 1f, 0f, 1f) : 1f;

        Backgrounds.drawCover(g, loading, w, h, globalAlpha);
        if (f > 0f) {
            ResourceLocation transition = Backgrounds.get(ch.file(Figure.Slot.TRANSITION));
            Backgrounds.drawCover(g, transition, w, h, Mth.clamp(f, 0f, 1f) * globalAlpha);
        }
    }
}
