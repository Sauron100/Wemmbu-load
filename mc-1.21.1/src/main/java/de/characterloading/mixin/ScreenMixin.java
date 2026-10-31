package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces the dirt / panorama / blurred-world background of every screen. */
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void characterloading$background(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Backgrounds.drawFor((Screen) (Object) this, context)) ci.cancel();
    }
}
