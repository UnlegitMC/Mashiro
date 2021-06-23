package me.liuli.mashiro.inject.mixins.render;

import me.liuli.mashiro.Mashiro;
import me.liuli.mashiro.event.TextEvent;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @ModifyVariable(method = "renderString", at = @At("HEAD"), ordinal = 0)
    private String renderString(final String string) {
        if (string == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        Mashiro.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), ordinal = 0)
    private String getStringWidth(final String string) {
        if (string == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        Mashiro.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }
}
