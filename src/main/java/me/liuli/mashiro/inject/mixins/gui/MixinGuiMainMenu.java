package me.liuli.mashiro.inject.mixins.gui;

import me.liuli.mashiro.Mashiro;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends MixinGuiScreen {
    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        // render client brand
        this.fontRendererObj.drawString(Mashiro.getColoredName()+" v"+Mashiro.getVersion(), 2, this.height - (10 + 4 * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215, false);

//        Mashiro.altManager.drawNowAltOnMenu();
    }
}
