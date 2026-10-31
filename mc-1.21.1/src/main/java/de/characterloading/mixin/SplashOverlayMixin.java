package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import de.characterloading.Figure;
import de.characterloading.Config;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the character artwork over the Mojang start-up screen (and every F3+T reload).
 * While it fades out, it cross-fades into the "transition" image, which then melts into the title screen.
 */
@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
    @Shadow private long reloadCompleteTime;

    @Inject(method = "render", at = @At("TAIL"))
    private void characterloading$render(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Config cfg = Config.get();
        if (!cfg.enabled) return;
        Figure ch = cfg.character();
        Identifier loading = Backgrounds.get(ch.file(Figure.Slot.LOADING));
        if (loading == null) return;

        int w = ctx.getScaledWindowWidth(), h = ctx.getScaledWindowHeight();
        long now = Util.getMeasuringTimeMs();
        float f = reloadCompleteTime > -1L ? (now - reloadCompleteTime) / 1000f : -1f;
        float globalAlpha = f >= 1f ? 1f - MathHelper.clamp(f - 1f, 0f, 1f) : 1f;

        Backgrounds.drawCover(ctx, loading, w, h, globalAlpha);
        if (f > 0f) {
            Identifier transition = Backgrounds.get(ch.file(Figure.Slot.TRANSITION));
            Backgrounds.drawCover(ctx, transition, w, h, MathHelper.clamp(f, 0f, 1f) * globalAlpha);
        }
    }
}
