package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** World/server/options lists draw their own dirt panel – make it translucent so the artwork shines through. */
@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Inject(method = "drawMenuListBackground", at = @At("HEAD"), cancellable = true)
    private void characterloading$listBackground(DrawContext context, CallbackInfo ci) {
        if (!Backgrounds.active() || MinecraftClient.getInstance().currentScreen == null) return;
        EntryListWidget<?> self = (EntryListWidget<?>) (Object) this;
        context.fill(self.getX(), self.getY(), self.getX() + self.getWidth(), self.getY() + self.getHeight(), 0x80000000);
        ci.cancel();
    }

    @Inject(method = "drawHeaderAndFooterSeparators", at = @At("HEAD"), cancellable = true)
    private void characterloading$separators(DrawContext context, CallbackInfo ci) {
        if (Backgrounds.active() && MinecraftClient.getInstance().currentScreen != null) ci.cancel();
    }
}
