package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces the dirt / panorama / blurred-world background of every screen. */
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void characterloading$background(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Backgrounds.drawFor((Screen) (Object) this, graphics)) ci.cancel();
    }
}
