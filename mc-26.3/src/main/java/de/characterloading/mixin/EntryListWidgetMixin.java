package de.characterloading.mixin;

import de.characterloading.Backgrounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** World/server lists draw their own dirt panel - make it translucent so the artwork shines through. */
@Mixin(AbstractSelectionList.class)
public abstract class EntryListWidgetMixin {
    @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
    private void characterloading$listBackground(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (!Backgrounds.active() || Minecraft.getInstance().screen == null) return;
        AbstractSelectionList<?> self = (AbstractSelectionList<?>) (Object) this;
        graphics.fill(self.getX(), self.getY(), self.getX() + self.getWidth(), self.getY() + self.getHeight(), 0x80000000);
        ci.cancel();
    }

    @Inject(method = "extractListSeparators", at = @At("HEAD"), cancellable = true)
    private void characterloading$separators(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (Backgrounds.active() && Minecraft.getInstance().screen != null) ci.cancel();
    }
}
