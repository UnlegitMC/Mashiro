package me.liuli.mashiro.inject.mixins.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Shadow
    protected FontRenderer fontRendererObj;

    @Shadow
    public int width;

    @Shadow
    public int height;
}
