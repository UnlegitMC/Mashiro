package me.liuli.mashiro.inject.mixins.gui;

import me.liuli.mashiro.Mashiro;
import me.liuli.mashiro.event.GuiKeyEvent;
import me.liuli.mashiro.event.Render2DEvent;
import me.liuli.mashiro.event.RenderScreenEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.GuiModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Shadow
    protected FontRenderer fontRendererObj;

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        Mashiro.eventManager.callEvent(new RenderScreenEvent(mouseX, mouseY, partialTicks));
    }

    @Inject(method = "keyTyped", at = @At("RETURN"))
    private void keyTyped(char typedChar, int keyCode, CallbackInfo callbackInfo) {
        Mashiro.eventManager.callEvent(new GuiKeyEvent(typedChar, keyCode));
    }

//    @Inject(method = "mouseClicked", at = @At("RETURN"))
//    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo callbackInfo) {
//        if(Mashiro.ultralightManager!=null)
//            Mashiro.ultralightManager.getEventAdaptor().onMouseClick(mouseX, mouseY, mouseButton);
//    }
}
